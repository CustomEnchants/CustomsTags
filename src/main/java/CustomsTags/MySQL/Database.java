package CustomsTags.MySQL;

import CustomsTags.CustomsTagsPlugin;

import java.sql.*;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL,
 * SQLite, etc.)
 *
 * @author -_Husky_-
 * @author tips48
 */
abstract class Database {

    /**
     * Plugin instance, use for plugin.getDataFolder()
     */
    public final CustomsTagsPlugin plugin = CustomsTagsPlugin.getInstance();
    protected Connection connection;

    /**
     * Creates a new Database
     */
    protected Database() {
        this.connection = null;
    }

    /**
     * Opens a connection with the database
     *
     * @throws SQLException           if the connection can not be opened
     * @throws ClassNotFoundException if the driver cannot be found
     */
    public abstract void openConnection() throws SQLException,
            ClassNotFoundException;

    /**
     * Checks if a connection is open with the database
     *
     * @return true if the connection is open
     * @throws SQLException if the connection cannot be checked
     */
    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    /**
     * Gets the connection with the database
     *
     * @return Connection with the database, null if none
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection with the database
     *
     * @return true if successful
     * @throws SQLException if the connection cannot be closed
     */
    public boolean closeConnection() throws SQLException {
        if (connection == null) {
            return false;
        }
        connection.close();
        return true;
    }


    /**
     * Executes a SQL Query<br>
     * <p>
     * If the connection is closed, it will be opened
     *
     * @param query Query to be run
     * @return the results of the query
     * @throws SQLException           If the query cannot be executed
     * @throws ClassNotFoundException If the driver cannot be found; see {@link #openConnection()}
     */
    public ResultSet querySQL(String query) throws SQLException,
            ClassNotFoundException {
        if (!checkConnection()) {
            openConnection();
        }

        Statement statement = connection.createStatement();

        return statement.executeQuery(query);
    }

    /**
     * Executes an Update SQL Query<br>
     * See {@link Statement#executeUpdate(String)}<br>
     * If the connection is closed, it will be opened
     *
     * @param query Query to be run
     * @return Result Code, see {@link Statement#executeUpdate(String)}
     * @throws SQLException           If the query cannot be executed
     * @throws ClassNotFoundException If the driver cannot be found; see {@link #openConnection()}
     */
    public int updateSQL(String query) throws SQLException,
            ClassNotFoundException {
        if (!checkConnection()) {
            openConnection();
        }

        Statement statement = connection.createStatement();

        return statement.executeUpdate(query);
    }

    public void setupDataTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS `" + "Tags" + "` (Registered INTEGER DEFAULT 1, MCUUID TEXT, TAG TEXT,PRIMARY KEY(MCUUID));";
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error while setting up Collector data table " + e.getMessage());
        }
    }

    public void insert(String uuid, String tag) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT OR IGNORE INTO Tags (Registered, MCUUID, Tag) VALUES (?, ?, ?);");
            ps.setInt(1, 1);
            ps.setString(2, uuid);
            ps.setString(3, tag);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void update(String uuid, String tag) {
        try {
            String sql = "UPDATE Tags SET Tag='%tag%' WHERE MCUUID='%uuid%';".replace("%uuid%", uuid).replace("%tag%", tag);
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getTag(String uuid) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT Tag FROM Tags WHERE MCUUID='%uuid%';".replace("%uuid%", uuid));
            ResultSet rs = st.executeQuery();
            return rs.getString(1);
        } catch (SQLException e) {
            return "";
        }
    }
}