/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.irc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultConsumer;
import org.apache.camel.util.ObjectHelper;
import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventAdapter;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IrcConsumer extends DefaultConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(IrcConsumer.class);

    private final IrcConfiguration configuration;
    private final IrcEndpoint endpoint;
    private final IRCConnection connection;
    private IRCEventAdapter listener = new FilteredIRCEventAdapter();

    public IrcConsumer(IrcEndpoint endpoint, Processor processor, IRCConnection connection) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.connection = connection;
        this.configuration = endpoint.getConfiguration();
    }

    @Override
    protected void doStop() throws Exception {
        if (connection != null) {
            for (IrcChannel channel : endpoint.getConfiguration().getChannelList()) {
                LOG.debug("Parting: {}", channel);
                connection.doPart(channel.getName());
            }
            connection.removeIRCEventListener(listener);
        }
        super.doStop();
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        listener = getListener();
        connection.addIRCEventListener(listener);

        LOG.debug("Sleeping for {} seconds before sending commands.", configuration.getCommandTimeout() / 1000);
        // sleep for a few seconds as the server sometimes takes a moment to fully connect, print banners, etc after connection established
        try {
            Thread.sleep(configuration.getCommandTimeout());
        } catch (InterruptedException ex) {
            LOG.info("Interrupted while sleeping before sending commands");
            Thread.currentThread().interrupt();
        }
        if (ObjectHelper.isNotEmpty(configuration.getNickPassword())) {
            LOG.debug("Identifying and enforcing nick with NickServ.");
            // Identify nick and enforce, https://meta.wikimedia.org/wiki/IRC/Instructions#Register_your_nickname.2C_identify.2C_and_enforce
            connection.doPrivmsg("nickserv", "identify " + configuration.getNickPassword());
            connection.doPrivmsg("nickserv", "set enforce on");
        }

        endpoint.joinChannels();
    }

    private Exchange createOnPrivmsgExchange(String target, IRCUser user, String msg) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "PRIVMSG", target, user, msg);
        exchange.setIn(im);
        return exchange;
    }

    private Exchange createOnNickExchange(IRCUser user, String newNick) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "NICK", user, newNick);
        exchange.setIn(im);
        return exchange;
    }

    private Exchange createOnQuitExchange(IRCUser user, String msg) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "QUIT", user, msg);
        exchange.setIn(im);
        return exchange;
    }

    private Exchange createOnJoinExchange(String channel, IRCUser user) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "JOIN", channel, user);
        exchange.setIn(im);
        return exchange;
    }

    private Exchange createOnKickExchange(String channel, IRCUser user, String whoWasKickedNick, String msg) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "KICK", channel, user, whoWasKickedNick, msg);
        exchange.setIn(im);
        return exchange;
    }

    private Exchange createOnModeExchange(String channel, IRCUser user, IRCModeParser modeParser) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "MODE", channel, user, modeParser.getLine());
        exchange.setIn(im);
        return exchange;
    }

    private Exchange createOnPartExchange(String channel, IRCUser user, String msg) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "PART", channel, user, msg);
        exchange.setIn(im);
        return exchange;
    }

    private Exchange createOnReplyExchange(int num, String value, String msg) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "REPLY", num, value, msg);
        exchange.setIn(im);
        return exchange;
    }

    private Exchange createOnTopicExchange(String channel, IRCUser user, String topic) {
        Exchange exchange = createExchange(true);
        exchange.setProperty(Exchange.BINDING, endpoint.getBinding());
        IrcMessage im = new IrcMessage(endpoint.getCamelContext(), "TOPIC", channel, user, topic);
        exchange.setIn(im);
        return exchange;
    }

    public IRCConnection getConnection() {
        return connection;
    }

    public IRCEventAdapter getListener() {
        return listener;
    }

    public void setListener(IRCEventAdapter listener) {
        this.listener = listener;
    }

    class FilteredIRCEventAdapter extends IRCEventAdapter {

        @Override
        public void onNick(IRCUser user, String newNick) {
            if (configuration.isOnNick()) {
                Exchange exchange = createOnNickExchange(user, newNick);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onQuit(IRCUser user, String msg) {
            if (configuration.isOnQuit()) {
                Exchange exchange = createOnQuitExchange(user, msg);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onJoin(String channel, IRCUser user) {
            if (configuration.isOnJoin()) {
                Exchange exchange = createOnJoinExchange(channel, user);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onKick(String channel, IRCUser user, String passiveNick, String msg) {

            // check to see if I got kick and if so rejoin if autoRejoin is on
            if (passiveNick.equals(connection.getNick()) && configuration.isAutoRejoin()) {
                endpoint.joinChannel(channel);
            }

            if (configuration.isOnKick()) {
                Exchange exchange = createOnKickExchange(channel, user, passiveNick, msg);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onMode(String channel, IRCUser user, IRCModeParser modeParser) {
            if (configuration.isOnMode()) {
                Exchange exchange = createOnModeExchange(channel, user, modeParser);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onPart(String channel, IRCUser user, String msg) {
            if (configuration.isOnPart()) {
                Exchange exchange = createOnPartExchange(channel, user, msg);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onReply(int num, String value, String msg) {
            if (configuration.isOnReply()) {
                Exchange exchange = createOnReplyExchange(num, value, msg);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onTopic(String channel, IRCUser user, String topic) {
            if (configuration.isOnTopic()) {
                Exchange exchange = createOnTopicExchange(channel, user, topic);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onPrivmsg(String target, IRCUser user, String msg) {
            if (configuration.isOnPrivmsg()) {
                Exchange exchange = createOnPrivmsgExchange(target, user, msg);
                try {
                    getProcessor().process(exchange);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        @Override
        public void onError(int num, String msg) {
        }

    }

}
