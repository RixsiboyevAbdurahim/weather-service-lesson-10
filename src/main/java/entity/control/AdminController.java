package entity.control;

import entity.models.CustomUser;
import entity.models.city.City;
import entity.models.city.CityDao;
import entity.models.inner.InnerJoinDao;
import entity.models.user.AuthUser;
import entity.models.user.AuthUserDao;
import entity.models.weather.Weather;
import entity.models.weather.WeatherDao;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminController {

    private final AuthUserDao authUserDao;
    private final CityDao cityDao;
    private final WeatherDao weatherDao;
    private final InnerJoinDao innerJoinDao;

    public AdminController(AuthUserDao authUserDao, CityDao cityDao, WeatherDao weatherDao, InnerJoinDao innerJoinDao) {
        this.authUserDao = authUserDao;
        this.cityDao = cityDao;
        this.weatherDao = weatherDao;
        this.innerJoinDao = innerJoinDao;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "/page_admin/admin";
    }

    @GetMapping("/listusers")
    @PreAuthorize("hasRole('ADMIN')")
    public String listUsers(Model model) {
        List<AuthUser> users = authUserDao.findAllUsers();
        model.addAttribute("users", users);
        return "/page_admin/listusers";
    }

    @PostMapping("/getuser")
    @PreAuthorize("hasRole('ADMIN')")
    public String addUser(@RequestParam("userId") Integer id, Model model, HttpSession session) {
        session.setAttribute("updateUserId", id);
        AuthUser user = authUserDao.getById(id);
        model.addAttribute("user", user);
        return "/page_admin/updateuser";
    }

    @PostMapping("/updateuser")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@ModelAttribute("user") AuthUser user, Model model, HttpSession session) {
        AuthUser authUser = authUserDao.findByUsername(user.getUsername()).orElse(null);
        if (authUser == null) {
            int id = (int) session.getAttribute("updateUserId");
            AuthUser originalUser = authUserDao.getById(id);
            originalUser.setUsername(user.getUsername());
            authUserDao.updateUser(originalUser);
            System.out.println("User updated Successfully");
            session.removeAttribute("updateUserId");
            return "redirect:/listusers";
        }
        model.addAttribute("user_exists", "user_exists");
        return "page_admin/updateuser";
    }

    @GetMapping("/listcities")
    @PreAuthorize("hasRole('ADMIN')")
    public String listCities(Model model) {
        List<City> cities = cityDao.findAll();
        model.addAttribute("cities", cities);
        return "/page_admin/listcities";
    }

    @GetMapping("/addcity")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCity(Model model) {
        return "/page_admin/addcity";
    }

    @PostMapping("/addcity")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCity(@RequestParam("cityName") String name, Model model) {
        cityDao.save(name);
        return "redirect:/listcities";
    }

    @GetMapping("/weather")
    @PreAuthorize("hasRole('ADMIN')")
    public String addWeather(HttpSession session) {
        return "/page_admin/addneweather";
    }

    @PostMapping("/weather")
    @PreAuthorize("hasRole('ADMIN')")
    public String addWeather(@RequestParam("cityId") Integer id, HttpSession session) {
        session.setAttribute("cityId", id);
        return "/page_admin/addneweather";
    }

    @PostMapping("/addneweather")
    @PreAuthorize("hasRole('ADMIN')")
    public String addWeather(@ModelAttribute Weather weather, HttpSession session) {
        int cityId = (int) session.getAttribute("cityId");
        weather.setCity_id(cityId);
        weather.setDate(LocalDate.now());
        weatherDao.save(weather);
        System.out.println("Weather added Successfully");
        session.removeAttribute("cityId");
        return "redirect:/listcities";
    }

    @PostMapping("/getweather")
    @PreAuthorize("hasRole('ADMIN')")
    public String getWeather(@RequestParam("cityId") Integer id, HttpSession session, Model model) {
        session.setAttribute("cityId", id);
        List<Weather> weatherList = weatherDao.findAllByCityId(id);
        City city = cityDao.findById(id);
        model.addAttribute("weatherList", weatherList);
        model.addAttribute("city", city.getName());
        return "/page_admin/listweather";
    }

    @GetMapping("/userdetails")
    @PreAuthorize("hasRole('ADMIN')")
    public String userdetails(Model model) {
        List<CustomUser> customUsers = innerJoinDao.getByRoleUser();
        model.addAttribute("customUsers", customUsers);
        return "/page_admin/userdetails";
    }
}
