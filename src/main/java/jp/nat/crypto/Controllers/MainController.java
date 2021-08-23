package jp.nat.crypto.Controllers;

import jp.nat.crypto.Models.Cryptocurrency;
import jp.nat.crypto.Services.CryptoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.UK);
        model.addAttribute("date", formatter.format(new Date()));
        model.addAttribute("cryptoes", CryptoService.getData());
        return "index";
    }


    @GetMapping("/search")
    public String search(@RequestParam("crypto") String crypto, Model model) {
        ArrayList<Cryptocurrency> cryptocurrencies = CryptoService.getData();
        Cryptocurrency requested = null;
        for (Cryptocurrency c : cryptocurrencies) {
            if (c.getName().equalsIgnoreCase(crypto)) {
                requested = c;
                break;
            }
        }
        model.addAttribute("cryptoName", crypto);
        model.addAttribute("crypto", requested);
        return "result";
    }
}
