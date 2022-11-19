package com.example.qrgenerateandvalidate.resource;

import com.example.qrgenerateandvalidate.model.DecodedQrResponse;
import com.example.qrgenerateandvalidate.model.GenerateQrRequest;
import com.example.qrgenerateandvalidate.service.QrCodeService;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class QrCodeResource {

    @Autowired
    private QrCodeService qrCodeService;

    @PostMapping(path = "/api/qr/generate", produces = MediaType.IMAGE_JPEG_VALUE)
    public void generateQr(@RequestBody GenerateQrRequest request, HttpServletResponse response) throws MissingRequestValueException, IOException, WriterException {
        if (request==null || request.getQrString()==null || request.getQrString().trim().equals("") ) {
            throw new MissingRequestValueException("QR String is required");
        }
        qrCodeService.generateQr(request.getQrString(), response.getOutputStream());
        response.getOutputStream().flush();
    }

    @PostMapping(path = "/api/qr/decode")
    public DecodedQrResponse decodeQr(@RequestParam("qrCode") MultipartFile qrCode) throws IOException, NotFoundException {
        String qrCodeString =  qrCodeService.decodeQr(qrCode.getBytes());
        return new DecodedQrResponse(qrCodeString);

    }
}
