package ru.mipt.cybersecurity.pqc.crypto.sphincs;

import ru.mipt.cybersecurity.crypto.StreamCipher;
import ru.mipt.cybersecurity.crypto.engines.ChaChaEngine;
import ru.mipt.cybersecurity.crypto.params.KeyParameter;
import ru.mipt.cybersecurity.crypto.params.ParametersWithIV;
import ru.mipt.cybersecurity.util.Pack;

class Seed
{

    static void get_seed(HashFunctions hs, byte[] seed, int seedOff, byte[] sk, Tree.leafaddr a)
    {
        byte[] buffer = new byte[SPHINCS256Config.SEED_BYTES + 8];
        long t;
        int i;

        for (i = 0; i < SPHINCS256Config.SEED_BYTES; i++)
        {
            buffer[i] = sk[i];
        }

        //4 bits to encode level
        t = a.level;
        //55 bits to encode subtree
        t |= a.subtree << 4;
        //5 bits to encode leaf
        t |= a.subleaf << 59;

        Pack.longToLittleEndian(t, buffer, SPHINCS256Config.SEED_BYTES);

        hs.varlen_hash(seed, seedOff, buffer, buffer.length);
    }



    static void prg(byte[] r, int rOff, long rlen, byte[] key, int keyOff)
    {
        byte[]  nonce = new byte[8];

        StreamCipher cipher = new ChaChaEngine(12);

        cipher.init(true, new ParametersWithIV(new KeyParameter(key, keyOff, 32), nonce));

        cipher.processBytes(r, rOff, (int)rlen, r, rOff);

        //crypto_stream_chacha12(r, rlen, nonce, key);
    }
}
