package web.commands;

import business.exceptions.UserException;
import business.services.BmiUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CalcBMICommand extends CommandUnprotectedPage
{
    public CalcBMICommand(String pageToShow) //this pageToShow is the name of the jsp page that we want the command to fetch.
    {
        super(pageToShow);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws UserException
    {
        Double height = 0.0;
        Double weight = 0.0;
        Double bmi = 0.0;
        String category = "";

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

        return pageToShow;
    }
}
