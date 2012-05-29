package de.mcdorians.codebase.locbano;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LocbanoServlet extends HttpServlet {

	private static final String NEW = "n";
	private static final String TOKEN = "t";
	private static final String GEO = "g";
	private static final String ID = "id";
	private static final String TEXT = "txt";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		PrintWriter out = response.getWriter();

		response.setContentType("text/plain");

		String newnb = request.getParameter(NEW);
		String token = request.getParameter(TOKEN);
		String geo = request.getParameter(GEO);
		String id = request.getParameter(ID);
		String text = request.getParameter(TEXT);

		double lat = 0, lng = 0;

		int action = 0;
		if (token != null) {
			action += 1;
		}
		if (geo != null && geo.contains("I")) {
			action += 10;
			String[] g = geo.split("I");
			lat = Double.parseDouble(g[0]);
			lng = Double.parseDouble(g[1]);
		}
		if (id != null) {
			action += 100;
		}
		if (text != null) {
			action += 1000;
		}
		if (newnb != null) {
			action += 10000;
		}

		NotebookDAO dao = new NotebookDAO();
		Notebook nb;

		switch (action) {
		case 0:
			out.println("Errorcode: " + action);
			break;

		case 11:
			// request for notes
			nb = dao.getNotebook(token);
			if (nb != null) {
				out.println(JSONResponse.getJSONResponse(nb.getNotes(lat, lng)));
			} else {
				out.println("Error: Wrong Token");
			}
			break;
		case 1011:
			// add new note
			nb = dao.getNotebook(token);
			if (nb != null) {
				Note n = new Note(text, lat, lng);
				dao.ofy().put(n);
				nb.getNotesIds().add(n.getId());
				dao.ofy().put(nb);
				out.println(JSONResponse.getJSONResponse(n));
			} else {
				out.println("Error: Wrong Token");
			}
			break;
		case 101:
			// delete note
			Note n1 = dao.getNote(Long.parseLong(id));
			if (n1 != null) {
				dao.ofy().delete(n1);
				out.println("[]");
			} else {
				out.println("Error: Wrong Id");
			}

			break;
		case 1101:
			// update note
			Note n = dao.getNote(Long.parseLong(id));
			if (n != null) {
				n.setText(text);
				dao.ofy().put(n);
				out.println("[]");
			} else {
				out.println("Error: Wrong Id");
			}
			break;
		case 10000:
			// create new Notebook, return new token
			String t = TokenGenerator.generateToken();
			nb = dao.getNotebook(t);
			if (nb != null) {
				out.println("Error: tokencreation failed");
			} else {
				nb = new Notebook(t);
				dao.ofy().put(nb);
				out.println(JSONResponse.getJSONResponse(t));
			}
			break;

		default:
			out.println("Errorcode: " + action);
			break;
		}
	}

}
