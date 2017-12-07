package ru.mipt.cybersecurity.pqc.jcajce.provider.test;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;

import ru.mipt.cybersecurity.pqc.asn1.PQCObjectIdentifiers;
import ru.mipt.cybersecurity.pqc.jcajce.spec.McElieceKeyGenParameterSpec;


public class McElieceKeyPairGeneratorTest
    extends KeyPairGeneratorTest
{

    protected void setUp()
    {
        super.setUp();
    }

    public void testKeyFactory()
        throws Exception
    {
        kf = KeyFactory.getInstance("McEliece");
        kf = KeyFactory.getInstance(PQCObjectIdentifiers.mcEliece.getId());
    }

    public void testKeyPairEncoding_9_33()
        throws Exception
    {
        kf = KeyFactory.getInstance("McEliece");

        kpg = KeyPairGenerator.getInstance("McEliece");
        McElieceKeyGenParameterSpec params = new McElieceKeyGenParameterSpec(9, 33);
        kpg.initialize(params);
        performKeyPairEncodingTest(kpg.generateKeyPair());
    }

}