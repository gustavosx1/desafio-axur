
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AnalisadorHTML {

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
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!line.startsWith("</") && line.startsWith("<")) {
          contador++;
        } else if (line.startsWith("</")) {
          contador--;
        } else if (contador > profundidade) {
          profundidade = contador;
          resultado = line;
        }
      }
      if (contador != 0) {
        System.out.println("Sintaxe de HTML mal formatada!");
      }

      reader.close();

      System.out.printf("\nLinha mais profunda: %s", resultado);
      System.out.printf("\nProfundidade da Linha: %d", profundidade);

    } catch (Exception e) {
      System.out.println("URL connection error");
    }
  }
}
