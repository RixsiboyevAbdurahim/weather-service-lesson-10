package entity.control;

import entity.models.city.City;
import entity.models.city.CityDao;
import entity.models.inner.InnerJoinDao;
import entity.models.subscription.Subscription;
import entity.models.subscription.SubscriptionDao;
import entity.models.user.AuthUser;
import entity.models.user.AuthUserDao;
import entity.models.weather.Weather;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    private final CityDao cityDao;
    private final AuthUserDao authUserDao;
    private final SubscriptionDao subscriptionDao;
    private final InnerJoinDao innerJoinDao;

    public UserController(CityDao cityDao, AuthUserDao authUserDao, SubscriptionDao subscriptionDao, InnerJoinDao innerJoinDao) {
        this.cityDao = cityDao;
        this.authUserDao = authUserDao;
        this.subscriptionDao = subscriptionDao;
        this.innerJoinDao = innerJoinDao;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user(HttpSession session) {
        var name = SecurityContextHolder.getContext().getAuthentication().getName();
        AuthUser authUser = authUserDao.findByUsername(name).orElse(null);
        if (Objects.nonNull(authUser)) session.setAttribute("userId", authUser.getId());
        return "/page_user/user";
    }

    @GetMapping("/citiesuser")
    @PreAuthorize("hasRole('USER')")
    public String listCities(Model model) {
        List<City> cities = cityDao.findAll();
        model.addAttribute("cities", cities);
        return "/page_user/listcities";
    }

    @GetMapping("/subscription")
    @PreAuthorize("hasRole('USER')")
    public String listSubscriptions(Model model) {
        return "/page_user/subscription";
    }

    @PostMapping("/subscribe")
    @PreAuthorize("hasRole('USER')")
    public String subscribe(@RequestParam("cityId") int cityId, HttpSession session, Model model) {
        int userId = (int) session.getAttribute("userId");
        Subscription subscription = Subscription.builder()
                .user_id(userId)
                .city_id(cityId)
                .subscription_date(LocalDate.now()).build();
        List<Subscription> list = subscriptionDao.findAll();
        for (Subscription subs : list) {
            if (!Objects.equals(subs.getUser_id(), userId)) {
                subscriptionDao.save(subscription);
                model.addAttribute("subscription", "Subscriped successfully");
                return "/page_user/subscription";
            }
        }
        model.addAttribute("subscription", "You are already subscribed!");
        return "/page_user/subscription";
    }

    @GetMapping("/getsubscriptions")
    @PreAuthorize("hasRole('USER')")
    public String getSubscriptions(Model model, HttpSession session) {
        int userId = (int) session.getAttribute("userId");
        List<Weather> weathers = innerJoinDao.getAllWeatherByUserId(userId);
        model.addAttribute("weathers", weathers);
        return "/page_user/subscriptionlist";
    }

}
