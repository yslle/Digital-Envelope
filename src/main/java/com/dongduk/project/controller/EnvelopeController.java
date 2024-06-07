package com.dongduk.project.controller;

import com.dongduk.project.domain.Envelope;
import com.dongduk.project.domain.dto.CreateSignDTO;
import com.dongduk.project.domain.dto.VerifySignDTO;
import com.dongduk.project.service.EnvelopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/envelopes")
public class EnvelopeController {

    private final EnvelopeService envelopeService;

    // 전자봉투 보내기
    @GetMapping
    public String sendDigitalSign(Model model) {
        model.addAttribute("createSignDTO", new CreateSignDTO());
        return "sendEnvelopeForm";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String sendDigitalSign(@ModelAttribute("createSignDTO") CreateSignDTO createSignDTO, Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "createKeyForm";
        }

        Envelope envelope = null;

        try {
            envelope = envelopeService.sendDigitalSign(createSignDTO);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException("전자서명 암호화 중 오류가 발생하였습니다.", e);
        }


        if (envelope != null) {
            model.addAttribute("message", "전자봉투가 전송되었습니다.");
        } else {
            model.addAttribute("message", "전자봉투 전송에 실패하였습니다.");
        }

        model.addAttribute("createSignDTO", createSignDTO);
        return "sendEnvelopeForm";
    }

    // 전자봉투 확인하기
    @GetMapping("/list")
    public String findEnvelopeList() {
        return "receiveEnvelopeForm";
    }

    @PostMapping("/list")
    public String findEnvelopeList(@RequestParam String receiver, Model model) {
        List<Envelope> envelopeList = envelopeService.findEnvelopeList(receiver);
        model.addAttribute("envelopeList", envelopeList);

        return "receiveEnvelopeForm";
    }

    @GetMapping("/list/{envelopeId}")
    public String verifySign(@PathVariable Long envelopeId, @RequestParam String receiver, Model model) {
        VerifySignDTO verifySignDTO = envelopeService.verifySign(envelopeId,receiver);
        model.addAttribute("verifySignDTO", verifySignDTO);

        return "envelopeDetail";
    }
}
