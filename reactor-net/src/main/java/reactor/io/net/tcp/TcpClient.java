/*
 * Copyright (c) 2011-2014 Pivotal Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package reactor.io.netty.tcp;

import reactor.Environment;
import reactor.core.Dispatcher;
import reactor.fn.Supplier;
import reactor.io.buffer.Buffer;
import reactor.io.codec.Codec;
import reactor.io.netty.ChannelStream;
import reactor.io.netty.ReactorClient;
import reactor.io.netty.config.ClientSocketOptions;
import reactor.io.netty.config.SslOptions;

import java.net.InetSocketAddress;

/**
 * The base class for a Reactor-based TCP client.
 *
 * @param <IN>
 * 		The type that will be received by this client
 * @param <OUT>
 * 		The type that will be sent by this client
 *
 * @author Jon Brisbin
 * @author Stephane Maldini
 */
public abstract class TcpClient<IN, OUT>
		extends ReactorClient<IN, OUT, ChannelStream<IN, OUT>> {

	protected final Supplier<InetSocketAddress>            connectAddress;
	private final ClientSocketOptions options;
	private final SslOptions          sslOptions;

	protected TcpClient(Environment env,
	                    Dispatcher dispatcher,
	                   Supplier<InetSocketAddress> connectAddress,
	                    ClientSocketOptions options,
	                    SslOptions sslOptions,
	                    Codec<Buffer, IN, OUT> codec) {
		super(env, dispatcher, codec, options.prefetch());
		if(connectAddress == null){
			final InetSocketAddress loopback = new InetSocketAddress("127.0.0.1", 3000);
			this.connectAddress = new Supplier<InetSocketAddress>() {
				@Override
				public InetSocketAddress get() {
					return loopback;
				}
			};
		}
		else{
			this.connectAddress = connectAddress;
		}
		this.options = options;
		this.sslOptions = sslOptions;
	}

	/**
	 * Get the {@link java.net.InetSocketAddress} to which this client must connect.
	 *
	 * @return the connect address
	 */
	public InetSocketAddress getConnectAddress() {
		return connectAddress.get();
	}

	/**
	 * Get the {@link reactor.io.netty.config.ClientSocketOptions} currently in effect.
	 *
	 * @return the client options
	 */
	protected ClientSocketOptions getOptions() {
		return this.options;
	}

	/**
	 * Get the {@link reactor.io.netty.config.SslOptions} current in effect.
	 *
	 * @return the SSL options
	 */
	protected SslOptions getSslOptions() {
		return sslOptions;
	}

}
