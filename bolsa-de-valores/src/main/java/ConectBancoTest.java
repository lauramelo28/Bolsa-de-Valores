import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe para conexão com o banco de dados MySQL
 */
public class ConectBancoTest {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://monorail.proxy.rlwy.net:11973/bolsa_valores";
        String username = "root";
        String password = "ohMXOmrbZxCiNjBrtOszTpQHKEKPgqnr";

        // Estabelecer conexão
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            System.out.println("Conexão com o banco de dados MySQL estabelecida com sucesso!");
            Statement statement = connection.createStatement();

            // Executar a consulta
            String sql = "SELECT * FROM transacao";
            ResultSet resultSet = statement.executeQuery(sql);

            // Processar os resultados
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String idAtivo = resultSet.getString("idAtivo");
                // Obtenha outros campos conforme necessário e imprima ou processe-os como desejado
                System.out.println("ID: " + id + ", ID do Ativo: " + idAtivo);
            }
        } catch (SQLException e) {
            System.err.println("Falha ao conectar ao banco de dados MySQL:");
            e.printStackTrace();
        }
    }
}
