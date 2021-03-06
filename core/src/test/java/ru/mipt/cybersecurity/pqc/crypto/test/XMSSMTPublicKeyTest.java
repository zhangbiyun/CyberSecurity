package ru.mipt.cybersecurity.pqc.crypto.test;

import java.io.IOException;
import java.security.SecureRandom;

import junit.framework.TestCase;
import ru.mipt.cybersecurity.crypto.digests.SHA256Digest;
import ru.mipt.cybersecurity.pqc.crypto.xmss.XMSSMT;
import ru.mipt.cybersecurity.pqc.crypto.xmss.XMSSMTParameters;
import ru.mipt.cybersecurity.pqc.crypto.xmss.XMSSMTPublicKeyParameters;
import ru.mipt.cybersecurity.util.Arrays;

/**
 * Test cases for XMSSMTPublicKey class.
 * 
 */
public class XMSSMTPublicKeyTest extends TestCase {

	public void testPublicKeyParsingSHA256() throws IOException, ClassNotFoundException {
		XMSSMTParameters params = new XMSSMTParameters(20, 10, new SHA256Digest());
		XMSSMT mt = new XMSSMT(params, new SecureRandom());
		mt.generateKeys();
		byte[] privateKey = mt.exportPrivateKey();
		byte[] publicKey = mt.exportPublicKey();

		mt.importState(privateKey, publicKey);

		assertTrue(Arrays.areEqual(publicKey, mt.exportPublicKey()));
	}

	public void testConstructor() {
		XMSSMTParameters params = new XMSSMTParameters(20, 10, new SHA256Digest());
		XMSSMTPublicKeyParameters pk = new XMSSMTPublicKeyParameters.Builder(params).build();

		byte[] pkByte = pk.toByteArray();
		/* check everything is 0 */
		for (int i = 0; i < pkByte.length; i++) {
			assertEquals(0x00, pkByte[i]);
		}
	}
}
