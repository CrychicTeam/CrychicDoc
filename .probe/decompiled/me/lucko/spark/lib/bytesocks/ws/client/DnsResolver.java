package me.lucko.spark.lib.bytesocks.ws.client;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public interface DnsResolver {

    InetAddress resolve(URI var1) throws UnknownHostException;
}