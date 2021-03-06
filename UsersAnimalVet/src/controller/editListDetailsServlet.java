package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ListDetails;
import model.Owner;
import model.PetList;

/**
 * Servlet implementation class editListDetailsServlet
 */
@WebServlet("/editListDetailsServlet")
public class editListDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public editListDetailsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ListDetailsHelper dao = new ListDetailsHelper();
		PetListHelper lih = new PetListHelper();
		OwnerHelper sh = new OwnerHelper();
		
		
		Integer tempId = Integer.parseInt(request.getParameter("id"));
		ListDetails listToUpdate = dao.searchForListDetailsById(tempId);

		String newListName = request.getParameter("listName");
		String month = request.getParameter("month");
		String day = request.getParameter("day");
		String year = request.getParameter("year");
		
		String ownerPNum = request.getParameter("ownerPNum");
		//find our add the new shopper
		Owner newOwner = sh.findOwner(ownerPNum);

		LocalDate ld;
		try {
			ld = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
		} catch (NumberFormatException ex) {
			ld = LocalDate.now();
		}

		try {
			//items are selected in list to add
			String[] selectedPets = request.getParameterValues("allPetsToAdd");
			List<PetList> selectedPetsInList = new ArrayList<PetList>();

			for (int i = 0; i < selectedPets.length; i++) {
				System.out.println(selectedPets[i]);
				PetList c = lih.searchForPetById(Integer.parseInt(selectedPets[i]));
				selectedPetsInList.add(c);

			}
			listToUpdate.setListOfPets(selectedPetsInList);
		} catch (NullPointerException ex) {
			// no items selected in list - set to an empty list
			List<PetList> selectedPetsInList = new ArrayList<PetList>();
			listToUpdate.setListOfPets(selectedPetsInList);
		}

		listToUpdate.setListName(newListName);
		listToUpdate.setVisitDate(ld);
		listToUpdate.setOwner(newOwner);

		dao.updateList(listToUpdate);

		getServletContext().getRequestDispatcher("/viewAllListsServlet").forward(request, response);
	}

}
