package pl.edu.wszib.jwd.jwdcolors.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.edu.wszib.jwd.jwdcolors.helper.ColorHelper;
import pl.edu.wszib.jwd.jwdcolors.model.SelectedColor;
import pl.edu.wszib.jwd.jwdcolors.service.SelectedColorService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SelectedColorController {

    @Autowired
    SelectedColorService selectedColorService;

    public static final String SELECT_COLOR_TITLE = "Wybierz kolor";
    public static final String DATA_TITLE = "Dane";
    public static final String STAT_TITLE = "Statystyki";

    @GetMapping({"/select/{color}","/select"})
    public String selectColorPage(@PathVariable(required = false) String color, Model model) {

        if (color != null) {
            selectedColorService.save(color);
        }

        String[][] colors = {
                {"red","blue","purple","teal"},
                {"black","orange","yellow","green"},
                {"gray","silver","olive","lime"},
                {"navy","white","aqua","fuchsia"}
        };

        model.addAttribute("title", SELECT_COLOR_TITLE);
        model.addAttribute("colors", colors);
        return "select";
    }

    @GetMapping("/data")
    public String dataPage(Model model) {
        model.addAttribute("title", DATA_TITLE);
        model.addAttribute("selectedColors", selectedColorService.getAllData());
        return "data";
    }

    @GetMapping("/stat")
    public String statPage(Model model) {
        List<SelectedColor> selectedColors = (List)selectedColorService.getAllData();

        //Można to zmapować w dowolny inny sposób, np iterując i zaliczając
        Map<String, Long> dataMap = selectedColors.stream().collect(Collectors.groupingBy(SelectedColor::getColor, Collectors.counting()));

        model.addAttribute("title", STAT_TITLE);
        model.addAttribute("labels", dataMap.keySet());
        model.addAttribute("values", dataMap.values());
        model.addAttribute("backgrounds", ColorHelper.convertColors(dataMap.keySet()));
        return "stat";
    }

    @GetMapping("/")
    public String showStartPage() {
        return "redirect:select";
    }
}
