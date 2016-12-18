package com.bolyartech.forge.server.modules.user.data;

import com.google.gson.annotations.SerializedName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserJson {
    @SerializedName("id")
    public final long mId;
    @SerializedName("username")
    private final String mUsername;
    @SerializedName("screen_name")
    private final String mScreenName;
    @SerializedName("disabled")
    private final boolean mIsDisabled;


    public UserJson(User user, String screenName) {
        mId = user.getId();
        mUsername = user.getUsername();
        mScreenName = screenName;
        mIsDisabled = user.isDisabled();
    }


    public UserJson(long userId, String username, String screenName, boolean isDisabled) {
        mId = userId;
        mUsername = username;
        mScreenName = screenName;
        mIsDisabled = isDisabled;
    }


    public static List<UserJson> list(Connection dbc, long idGreaterThan, int pageSize) throws SQLException {
        String sql = "SELECT users.id as uid, users.username, users.is_disabled, user_screen_names.screen_name " +
                "FROM users LEFT OUTER JOIN user_screen_names ON users.id = user_screen_names.user " +
                "WHERE users.id > ? LIMIT ?";

        List<UserJson> ret = new ArrayList<>();
        try (PreparedStatement st = dbc.prepareStatement(sql)) {
            st.setLong(1, idGreaterThan);
            st.setLong(2, pageSize);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                UserJson tmp = new UserJson(rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4) == 1);
                ret.add(tmp);
            }
        }

        return ret;
    }
}
