package com.bolyartech.forge.server.modules.user.data;

import com.bolyartech.forge.server.db.DbUtils;

import java.sql.*;


public class ScreenName {
    private static final String ANONYMOUS_USER_PREFIX = "user";

    private final long mId;
    private final long mUserId;
    private final String mScreenName;
    private final String mScreenNameLc;


    public ScreenName(long id, long userId, String screenName) {
        mId = id;
        mUserId = userId;
        mScreenName = screenName;
        mScreenNameLc = screenName.toLowerCase();
    }


    public static ScreenName loadById(Connection dbc, long id) throws SQLException {
        DbUtils.ensureOperationalDbc(dbc);
        DbUtils.ensureValidId(id);

        String sql = "SELECT user, screen_name FROM user_screen_names WHERE id = ?";

        PreparedStatement psLoad = dbc.prepareStatement(sql);
        psLoad.setLong(1, id);
        ResultSet rs = psLoad.executeQuery();

        if (rs.next()) {
            return new ScreenName(id, rs.getLong(1), rs.getString(2));
        } else {
            return null;
        }
    }


    public static ScreenName loadByUser(Connection dbc, long userId) throws SQLException {
        DbUtils.ensureOperationalDbc(dbc);
        DbUtils.ensureValidId(userId);

        String sql = "SELECT id, screen_name FROM user_screen_names WHERE user = ?";

        PreparedStatement psLoad = dbc.prepareStatement(sql);
        psLoad.setLong(1, userId);
        ResultSet rs = psLoad.executeQuery();

        if (rs.next()) {
            return new ScreenName(rs.getLong(1), userId, rs.getString(2));
        } else {
            return null;
        }
    }


    public static boolean existsForUser(Connection dbc, long userId) throws SQLException {
        ScreenName sn = loadByUser(dbc, userId);
        return sn != null;
    }


    /**
     * @param dbc
     * @param userId
     * @param screenName
     * @return true if newly created
     * @throws SQLException
     */
    public static boolean setForUser(Connection dbc, long userId, String screenName) throws SQLException {
        if (existsForUser(dbc, userId)) {
            change(dbc, userId, screenName);
            return false;
        } else {
            createNew(dbc, userId, screenName);
            return true;
        }
    }


    @SuppressWarnings("UnusedReturnValue")
    private static boolean change(Connection dbc, long userId, String screenName) throws SQLException {
        DbUtils.ensureOperationalDbc(dbc);
        DbUtils.ensureValidId(userId);

        if (!isValid(screenName)) {
            throw new IllegalArgumentException("Invalid screen name: " + screenName);
        }


        String sql = "UPDATE user_screen_names SET screen_name = ?, screen_name_lc = ?";
        try (PreparedStatement st = dbc.prepareStatement(sql)) {
            st.setString(1, screenName);
            st.setString(2, screenName.toLowerCase());
            return st.executeUpdate() > 0;
        }
    }


    @SuppressWarnings("UnusedReturnValue")
    private static ScreenName createNew(Connection dbc, long userId, String screenName) throws SQLException {
        DbUtils.ensureOperationalDbc(dbc);
        DbUtils.ensureValidId(userId);


        if (!isValid(screenName)) {
            throw new IllegalArgumentException("Invalid screen name: " + screenName);
        }


        String sql = "INSERT INTO user_screen_names (user, screen_name, screen_name_lc) VALUES (?, ?, ?)";
        try (PreparedStatement st = dbc.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, userId);
            st.setString(2, screenName);
            st.setString(3, screenName.toLowerCase());
            st.execute();

            ResultSet res = st.getGeneratedKeys();
            if (res.next()) {
                return new ScreenName(res.getLong(1), userId, screenName);
            } else {
                throw new IllegalStateException("No generated ID for user_screen_names");
            }
        }
    }


    public static boolean exists(Connection dbc, String screenName) throws SQLException {
        DbUtils.ensureOperationalDbc(dbc);

        if (screenName == null) {
            throw new IllegalArgumentException("screenName is null");
        }

        String sql = "SELECT id FROM user_screen_names WHERE screen_name_lc = ?";

        PreparedStatement psLoad = dbc.prepareStatement(sql);
        psLoad.setString(1, screenName.toLowerCase());
        ResultSet rs = psLoad.executeQuery();

        return rs.next();
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValid(String screenName) {
        if (screenName == null) {
            return false;
        }

        return screenName.matches("^[\\p{L}][\\p{L}\\p{N} ]{1,33}[\\p{L}\\p{N}]$");
    }


    public static String createDefault(long userId) {
        return ANONYMOUS_USER_PREFIX + userId;
    }


    public long getId() {
        return mId;
    }


    public long getUserId() {
        return mUserId;
    }


    public String getScreenName() {
        return mScreenName;
    }


    public String getScreenNameLc() {
        return mScreenNameLc;
    }
}
