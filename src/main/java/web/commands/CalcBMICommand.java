package web.commands;

import business.exceptions.UserException;
import business.services.BmiFacade;
import business.services.BmiUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalcBMICommand extends CommandUnprotectedPage
{
    private BmiFacade bmiFacade;

    public CalcBMICommand(String pageToShow) //this pageToShow is the name of the jsp page that we want the command to fetch.
    {
        super(pageToShow);
        this.bmiFacade = new BmiFacade(database);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws UserException
    {
        int user_id = 1; //TODO: skal laves dynamisk ift. login.
        Double height = 0.0;
        Double weight = 0.0;
        Double bmi = 0.0;
        String category = "";
        String gender = request.getParameter("gender");
        int sport_id = Integer.parseInt(request.getParameter("sport"));

        String[] hobbies = request.getParameterValues("hobby");

        List<String> hobbyListStrings = null;

        if(hobbies != null){
            hobbyListStrings = Arrays.asList(hobbies);
        }

        List<Integer> hobbyListIntegers = new ArrayList<>();

        for (String hobbyListItem : hobbyListStrings)
        {
        hobbyListIntegers.add(Integer.parseInt(hobbyListItem));
        }

        try
        {
            // get parameters from the form on the index.jsp page
            height = Double.parseDouble(request.getParameter("height"));
            weight = Double.parseDouble(request.getParameter("weight"));
        }
        catch (NumberFormatException ex)
        {
            request.setAttribute("error", "Husk at du skal indtaste to heltal i formularen");
            return "index";
            //throw new UserException("Husk at du skal indtaste to heltal i formularen");
        }
// calculate BMI and BMI_category
        bmi = BmiUtil.calcBMI(height, weight);
        category = BmiUtil.getCategory(bmi);
// set attributes so we can fetch them from the jsp pages.
        request.setAttribute("bmi", String.format("%.2f", bmi));
        request.setAttribute("height", height);
        request.setAttribute("weight", weight);
        request.setAttribute("category", category);
        request.setAttribute("gender", gender);
        request.setAttribute("sport",sport_id);
        request.setAttribute("hobbies",hobbyListIntegers);



        bmiFacade.InsertBmiEntry(bmi,height,weight,category,gender,sport_id,user_id, hobbyListIntegers);





        return pageToShow;
    }
}
