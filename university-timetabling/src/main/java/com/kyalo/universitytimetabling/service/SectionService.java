package com.kyalo.universitytimetabling.service;

import com.kyalo.universitytimetabling.domain.Section;
import com.kyalo.universitytimetabling.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    public Section findById(Long id) {
        return sectionRepository.findById(id).orElse(null);
    }

    public Section save(Section section) {
        return sectionRepository.save(section);
    }

    public void deleteById(Long id) {
        sectionRepository.deleteById(id);
    }
}
