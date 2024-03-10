package ma.enset.tp2spring.web;

import lombok.AllArgsConstructor;
import ma.enset.tp2spring.entities.Patient;
import ma.enset.tp2spring.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
    @Controller
    @AllArgsConstructor
    public class PatientsRestService {

        private PatientRepository patientRepository;
        @GetMapping("/index")
        public  String index(Model model  ,
                             @RequestParam(name="page",defaultValue = "0") int p,
                             @RequestParam(name = "size", defaultValue = "4") int s,
                             @RequestParam(name = "keyword", defaultValue = "") String kw
                             ){
            Page<Patient> patientPage = patientRepository.findByNomContains(kw, PageRequest.of(p,s));

            model.addAttribute("listPatients",patientPage.getContent());
              model.addAttribute("pages", new int [patientPage.getTotalPages()]);
              model.addAttribute("currentPage", p);
              model.addAttribute("keyword",kw);

            return "patients";
        }

        @GetMapping("/delete")
        public String delete(Long id,String keyword, int page){
            patientRepository.deleteById(id);
            return "redirect:/index?page="+page+"&keyword="+keyword;

        }
    }

