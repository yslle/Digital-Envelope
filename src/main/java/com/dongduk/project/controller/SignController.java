package com.dongduk.project.controller;

import com.dongduk.project.domain.Envelope;
import com.dongduk.project.domain.dto.CreateSignDTO;
import com.dongduk.project.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/signs")
public class SignController {

    private final SignService signService;

    @GetMapping
    public String sendDigitalSign(Model model) {
        model.addAttribute("createSignDTO", new CreateSignDTO());
        return "sendSignForm";
    }

    @PostMapping
    public String sendDigitalSign(@ModelAttribute("createSignDTO") CreateSignDTO createSignDTO, Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "createKeyForm";
        }

        Envelope envelope = null;

        try {
            envelope = signService.sendDigitalSign(createSignDTO);
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
        return "sendSignForm";
    }
}
