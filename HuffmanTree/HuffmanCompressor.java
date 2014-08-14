import java.awt.Color;

import edu.neumont.io.Bits;
import edu.neumont.ui.Picture;


public class HuffmanCompressor {

	public byte[] compress(HuffmanTree tree, byte[] b)
	{
		Bits bits = new Bits();
		for(byte bt: b)
		{
			tree.fromByte(bt, bits);
		}
		while(bits.size()%8 != 0)
		{
			bits.add(false);
		}
		String binary = new String();
		for(Boolean bool: bits)
		{
			binary += (bool)?"1":"0";
		}
		int length = binary.length();
		byte[] results = new byte[(length+Byte.SIZE - 1) / Byte.SIZE];
		for(int i=0; i< length; i++)
		{
			if((binary.charAt(i)) == '1')
			{
				results[i/Byte.SIZE] = (byte) (results[i/ Byte.SIZE] | (0x80 >>>(i % Byte.SIZE)));
			}
		}
		return results;
	}
	
	public byte[] decompress(HuffmanTree tree, int uncompressedLength, byte[] b)
	{
		Bits bits = new Bits();
		for(int i=0; i<b.length*Byte.SIZE; i++)
		{
			bits.add((b[i/Byte.SIZE] << i % Byte.SIZE & 0x80) != 0);
		}
		byte[] results = new byte[uncompressedLength];
		int index = 0;
		while(bits.size() > 0 && index < results.length)
		{
			results[index++] = tree.toByte(bits);
		}
		return results;
	}
}
