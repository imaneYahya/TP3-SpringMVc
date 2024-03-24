package ma.enset.tp2spring.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.enset.tp2spring.entities.Patient;
import ma.enset.tp2spring.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        @GetMapping("/user/index")
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

        @GetMapping("/admin/delete")
        public String delete(Long id,String keyword, int page){
            patientRepository.deleteById(id);
            return "redirect:/user/index?page="+page+"&keyword="+keyword;

        }

        @GetMapping("/admin/formPatients")
        public String  formPatients(Model model){
            model.addAttribute("patient", new Patient());
            return  "formPatients";

        }
        @GetMapping("/admin/save")
        public String save(Model model , @Valid Patient patient , BindingResult bindingResult,
                          @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue ="" )  String  keyword){
        if(bindingResult.hasErrors())  return "formPatients";
            patientRepository.save(patient);
            return "redirect:/index?page="+page+"&keyword="+keyword;
        }

        @GetMapping("/admin/editPatient")
        public String  editPatient(Model model, Long id, String keyword , int page  ){
            Patient patient = patientRepository.findById(id).orElse(null);
            if(patient== null) throw new RuntimeException("Patinet introuvable ");
            model.addAttribute("patient", patient);
            model.addAttribute("page" , page);
            model.addAttribute("keyword", keyword);
            return  "editPatient";

        }

        @GetMapping("/")
        public String home(){
            return "redirect:/user/index ";
        }
    }

