
import java.net.URL;
import java.util.Stack;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class HtmlAnalyzer {

  public static void main(String[] args) {
    if (args.length != 1) {
      return;
    }

    String url = args[0];
    fetchHtml(url);
  }

  private static void fetchHtml(String urlString) {
    HttpURLConnection connection;

    try {
      URL url = new URL(urlString);
      connection = (HttpURLConnection) url.openConnection();

      BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream()));

      String resultado = "";
      String line = "";
      int profundidade = 0;
      int contador = 0;
      boolean malformedHtml = false;
      Stack<String> pilha = new Stack<>();

      while ((line = reader.readLine()) != null) {

        if ((line = line.trim()).isEmpty()) {
          continue;
        }

        if (!line.startsWith("</") && line.startsWith("<")) {
          contador++;
          pilha.push(line.substring(1, line.length() - 1));
        } else if (line.startsWith("</")) {
          contador--;
          if (contador < 0 || pilha.isEmpty()) {
            malformedHtml = true;
            break;
          }
          String closingTag = (line.substring(2, line.length() - 1));
          String openingTag = pilha.pop();
          if (!closingTag.equals(openingTag)) {
            malformedHtml = true;
            break;
          }
        } else {
          if (contador > profundidade) {
            profundidade = contador;
            resultado = line;
          }
        }
      }
      if (contador != 0 || malformedHtml == true) {
        System.out.println("malformed HTML");
        reader.close();
        return;
      }
      System.out.println(resultado);
      reader.close();

    } catch (Exception e) {
      System.out.println("URL connection error");
    }
  }
}
