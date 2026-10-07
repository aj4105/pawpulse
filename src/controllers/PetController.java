package controllers;

import model.Pet;
import model.User;
import operations.PetOperations;
import implementors.PetOperationsImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Handles pet profile CRUD.
 */
@WebServlet("/pet")
public class PetController extends HttpServlet {

    private final PetOperations petOps = new PetOperationsImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getLoggedInUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                try {
                    Pet pet = petOps.getPetById(Integer.parseInt(idStr), user.getUserId());
                    if (pet != null) {
                        req.setAttribute("pet", pet);
                        req.getRequestDispatcher("/pet-form.jsp").forward(req, resp);
                        return;
                    }
                } catch (Exception ignored) { }
            }
        }
        if ("delete".equals(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                try {
                    petOps.deletePet(Integer.parseInt(idStr), user.getUserId());
                } catch (Exception ignored) { }
            }
            resp.sendRedirect(req.getContextPath() + "/pets.jsp");
            return;
        }

        try {
            List<Pet> pets = petOps.getPetsByUserId(user.getUserId());
            req.setAttribute("pets", pets);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/pets.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = getLoggedInUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if ("add".equals(action) || "save".equals(action)) {
            Pet pet = new Pet();
            pet.setUserId(user.getUserId());
            pet.setName(req.getParameter("name"));
            pet.setBreed(req.getParameter("breed"));
            String ageStr = req.getParameter("age");
            pet.setAge(ageStr != null && !ageStr.isEmpty() ? Integer.parseInt(ageStr) : null);
            String weightStr = req.getParameter("weight");
            pet.setWeight(weightStr != null && !weightStr.isEmpty() ? Double.parseDouble(weightStr) : null);
            pet.setGender(req.getParameter("gender"));
            pet.setSpecies(req.getParameter("species") != null ? req.getParameter("species") : "Dog");

            String idStr = req.getParameter("petId");
            if (idStr != null && !idStr.isEmpty()) {
                pet.setPetId(Integer.parseInt(idStr));
                try {
                    petOps.updatePet(pet);
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                    req.setAttribute("pet", pet);
                    req.getRequestDispatcher("/pet-form.jsp").forward(req, resp);
                    return;
                }
            } else {
                try {
                    petOps.addPet(pet);
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                    req.setAttribute("pet", pet);
                    req.getRequestDispatcher("/pet-form.jsp").forward(req, resp);
                    return;
                }
            }
        }
        resp.sendRedirect(req.getContextPath() + "/pets.jsp");
    }

    private User getLoggedInUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        Object u = session.getAttribute("user");
        return (u instanceof User) ? (User) u : null;
    }
}
