package ru.mipt.cybersecurity.crypto.tls.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import ru.mipt.cybersecurity.crypto.tls.TlsServerProtocol;

class TlsTestServerProtocol extends TlsServerProtocol
{
    protected final TlsTestConfig config;

    public TlsTestServerProtocol(InputStream input, OutputStream output, SecureRandom secureRandom, TlsTestConfig config)
    {
        super(input, output, secureRandom);

        this.config = config;
    }
}
