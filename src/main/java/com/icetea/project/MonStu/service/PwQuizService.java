package com.icetea.project.MonStu.service;

import com.icetea.project.MonStu.domain.PwQuiz;
import com.icetea.project.MonStu.dto.PwQuizDTO;
import com.icetea.project.MonStu.repository.PwQuizRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PwQuizService {

    private PwQuizRepository pwQuizRsp;
    private final ModelMapper modelMapper;

    public PwQuizService(PwQuizRepository pwQuizRsp,ModelMapper modelMapper) {
        this.pwQuizRsp = pwQuizRsp;
        this.modelMapper = modelMapper;
    }

    //모든 데이터 가져오기
    public List<PwQuizDTO> findAllPwQuiz(){
        List<PwQuiz> modelList = pwQuizRsp.findAll();
        List<PwQuizDTO> dtoList = new ArrayList<PwQuizDTO>();
        for(PwQuiz i : modelList){
            dtoList.add(modelMapper.map(i,PwQuizDTO.class));
        }
        return dtoList;
    }

    //데이터 하나 저장
    public Boolean savePwQuiz(PwQuizDTO pwQDTO){
        PwQuiz pwQuiz = modelMapper.map(pwQDTO, PwQuiz.class);
        PwQuiz savedPwQuiz = pwQuizRsp.save(pwQuiz);
        return savedPwQuiz.getQid() != null;
    }




}
