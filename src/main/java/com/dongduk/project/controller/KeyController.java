package com.dongduk.project.controller;

import com.dongduk.project.domain.dto.CreateKeyDTO;
import com.dongduk.project.service.KeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/keys")
public class KeyController {

    private final KeyService keyService;

    @GetMapping
    public String createAndSaveKey(Model model) {
        model.addAttribute("createKeyDTO", new CreateKeyDTO());
        return "createKeyForm";
    }

    @PostMapping
    public String createAndSaveKey(@ModelAttribute("createKeyDTO") CreateKeyDTO createKeyDTO, Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "createKeyForm";
        }

        boolean isSuccess = keyService.createAndSaveKey(createKeyDTO);
        if (isSuccess) {
            model.addAttribute("message", "키 파일이 생성되었습니다.");
        } else {
            model.addAttribute("message", "키 파일 생성에 실패하였습니다.");
        }

        model.addAttribute("createKeyDTO", createKeyDTO);
        return "createKeyForm";
    }
}
