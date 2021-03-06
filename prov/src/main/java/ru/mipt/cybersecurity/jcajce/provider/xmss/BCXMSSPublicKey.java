package ru.mipt.cybersecurity.pqc.jcajce.provider.xmss;

import java.io.IOException;
import java.security.PublicKey;

import ru.mipt.cybersecurity.asn1.ASN1ObjectIdentifier;
import ru.mipt.cybersecurity.asn1.x509.AlgorithmIdentifier;
import ru.mipt.cybersecurity.asn1.x509.SubjectPublicKeyInfo;
import ru.mipt.cybersecurity.crypto.CipherParameters;
import ru.mipt.cybersecurity.pqc.asn1.PQCObjectIdentifiers;
import ru.mipt.cybersecurity.pqc.asn1.XMSSKeyParams;
import ru.mipt.cybersecurity.pqc.asn1.XMSSPublicKey;
import ru.mipt.cybersecurity.pqc.crypto.xmss.XMSSParameters;
import ru.mipt.cybersecurity.pqc.crypto.xmss.XMSSPublicKeyParameters;
import ru.mipt.cybersecurity.pqc.jcajce.interfaces.XMSSKey;
import ru.mipt.cybersecurity.util.Arrays;

public class BCXMSSPublicKey
    implements PublicKey, XMSSKey
{
    private final XMSSPublicKeyParameters keyParams;
    private final ASN1ObjectIdentifier treeDigest;

    public BCXMSSPublicKey(
        ASN1ObjectIdentifier treeDigest,
        XMSSPublicKeyParameters keyParams)
    {
        this.treeDigest = treeDigest;
        this.keyParams = keyParams;
    }

    public BCXMSSPublicKey(SubjectPublicKeyInfo keyInfo)
        throws IOException
    {
        XMSSKeyParams keyParams = XMSSKeyParams.getInstance(keyInfo.getAlgorithm().getParameters());
        this.treeDigest = keyParams.getTreeDigest().getAlgorithm();

        XMSSPublicKey xmssPublicKey = XMSSPublicKey.getInstance(keyInfo.parsePublicKey());

        this.keyParams = new XMSSPublicKeyParameters
            .Builder(new XMSSParameters(keyParams.getHeight(), DigestUtil.getDigest(treeDigest)))
            .withPublicSeed(xmssPublicKey.getPublicSeed())
            .withRoot(xmssPublicKey.getRoot()).build();
    }

    /**
     * @return name of the algorithm - "XMSS"
     */
    public final String getAlgorithm()
    {
        return "XMSS";
    }

    public byte[] getEncoded()
    {
        SubjectPublicKeyInfo pki;
        try
        {
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.xmss, new XMSSKeyParams(keyParams.getParameters().getHeight(), new AlgorithmIdentifier(treeDigest)));
            pki = new SubjectPublicKeyInfo(algorithmIdentifier, new XMSSPublicKey(keyParams.getPublicSeed(), keyParams.getRoot()));

            return pki.getEncoded();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public String getFormat()
    {
        return "X.509";
    }

    CipherParameters getKeyParams()
    {
        return keyParams;
    }

    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (o instanceof BCXMSSPublicKey)
        {
            BCXMSSPublicKey otherKey = (BCXMSSPublicKey)o;

            return treeDigest.equals(otherKey.treeDigest) && Arrays.areEqual(keyParams.toByteArray(), otherKey.keyParams.toByteArray());
        }

        return false;
    }

    public int hashCode()
    {
        return treeDigest.hashCode() + 37 * Arrays.hashCode(keyParams.toByteArray());
    }

    public int getHeight()
    {
        return keyParams.getParameters().getHeight();
    }

    public String getTreeDigest()
    {
        return DigestUtil.getXMSSDigestName(treeDigest);
    }
}
