
package com.wl.tools;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Component
public class QRImageGenerator {
	
	public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) {
        try {
        	
        	//prueba 1: BBC
        	//String qrCode = 3yf/19H4oj85Sak41s4dHrgROJC6St/rodOs0Ibxm5rdBCv07n+dHfKzESEfcar3dVreR4AW6QW7lxDllXaTe9jJ6hDS5kITiLUUTyw1vbhgtGLKDP75nD4J76cHDMDqYLkajLf4tzZRl8KEZYUBKQ/ExtaNHpePaEztvofZMId0dKxpANryNzjtXxFHbVKSbZD8MZJqj4Xx4ms2/JOxiQ=="";
        	//Prueba 2: BC
        	//String qrCode = "3yf/19H4oj85Sak41s4dHrgROJC6St/rodOs0Ibxm5rdBCv07n+dHfKzESEfcar3dVreR4AW6QW7lxDllXaTe9jJ6hDS5kITiLUUTyw1vbhgtGLKDP75nD4J76cHDMDqYLkajLf4tzZRl8KEZYUBKQ/ExtaNHpePaEztvofZMId0dKxpANryNzjtXxFHbVKSbZD8MZJqj4Xx4ms2/JOxiQ==";
            //Prueba 3: Bancolombia
        	//String qrCode = "3yf/19H4oj85Sak41s4dHrgROJC6St/rodOs0Ibxm5rdBCv07n+dHfKzESEfcar3dVreR4AW6QW7lxDllXaTe9jJ6hDS5kITiLUUTyw1vbhgtGLKDP75nD4J76cHDMDqYLkajLf4tzZRl8KEZYUBKQ/ExtaNHpePaEztvofZMId0dKxpANryNzjtXxFHbVKSbZD8MZJqj4Xx4ms2/JOxiQ==";
        	//Prueba 4 dejando vacios concept y accountToName
        	//String qrCode = "yq6JIzGbbIpwXcKqTtfDZQttYzxEtnuwML0+4MsuZX5A3MObrHHq3jiucdy6Q/EKHx5UmXC8S7MasBWki85PHbBXvNH8a+ScNEGNzzrfSMmWDLwTfcn6ntfn7TMP3bSktUv3rKKeuI30Bri5chQ1uxfr3jCeO6L051aABeFosPXU/oOhtxMGFFVM57EjJxoocAy+KWw1DgOcODnWC5b5qA==";
        	//Prueba 5 solo dejo vacios accountToName
        	//String qrCode = "yq6JIzGbbIpwXcKqTtfDZQttYzxEtnuwML0+4MsuZX5A3MObrHHq3jiucdy6Q/EKHx5UmXC8S7MasBWki85PHbBXvNH8a+ScNEGNzzrfSMmWDLwTfcn6ntfn7TMP3bSktUv3rKKeuI30Bri5chQ1u10/vWA+uMHOPlokDe6B32kILBIu3qx6xXFZTt07STichl+vnk57XQg+MVTF0slpBw==";
        	//Prueba 6 Con cuenta corriente sin llenar concept y accountToName
        	//String qrCode = "0123456789";
        	//100
        	//String qrCode = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
        	// Son iguales si cambia bankToCode, bankToName,
        	//Diferenrtes si cambia accountToName, monto, accountToOwner, accountToType, concept, amount
        	
        	String qrCode = "yq6JIzGbbIpwXcKqTtfDZQI1echnlLryMSfE7cklmJV6yhJjpiAg2fkaIOAVNhsb/Gfjp/g9XX29oZ34IG2WPjnRIUcwo1b6iC4mux3iem18SqotZFZVWBTKuYMkt9RWW5rFEiTl7eRR72vUOoUl7EGKRK1TrMyBVT6np/TAF6xhNprZ3gYFvRyqTHnGoHDNpf3Z5J9vcKAIEuEfd4PAAxRns36BFoSAkT2B30m6yG4wVyAY0W5eQDlEGPcowHFp5lyrvezzZtcGwMMLyO1E1Gg7b7ZTviNgoemaQR5dRa8=";
        	generateQRCodeImage(qrCode, 350, 350, "d:/code_qrs/qr2.png");
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }
}