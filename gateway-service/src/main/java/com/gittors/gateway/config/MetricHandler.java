package com.gittors.gateway.config;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
 
@ChannelHandler.Sharable
public class MetricHandler extends ChannelDuplexHandler {
 
    public AtomicLong totalConnectionNumber = new AtomicLong();
 
 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        totalConnectionNumber.incrementAndGet();
        super.channelActive(ctx);
    }
 
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        totalConnectionNumber.decrementAndGet();
        super.channelInactive(ctx);
    }
 
}
