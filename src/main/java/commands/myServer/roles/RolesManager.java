package commands.myServer.roles;

import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import utility.core.FileManager;

public class RolesManager {
	
	private static final String jsonData = FileManager.readFile("./assists/server_settings.json");
	private static final JSONObject role = new JSONObject(jsonData);

	public static String getRoleTypo(int i) {
		return getRoles().getJSONObject(i).getString("typo");
	}
	
	private static boolean isAssignable(int i) {
		return getRoles().getJSONObject(i).getBoolean("assignable");
	}
	
	public static String getRoleDes(int i) {
		return getRoles().getJSONObject(i).getString("description");
	}
	
	public static String getRoleName(int i) {
		return getRoles().getJSONObject(i).getString("name");
	}
	
	public static String getRoleType(int i) {
		return getRoles().getJSONObject(i).getString("section");
	}
	
	public static JSONArray getRoles() {
		return role.getJSONArray("roles");
	}
	
	public static boolean hasRole(Member member, String roleName) {
		for(Role role : member.getRoles()) {
			if(role.getName().toLowerCase().contains(roleName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean matchRole(String role, Guild gld) {
		List<Object> list = getRoles().toList();
		for(int i = 0; i < Math.max(gld.getRoles().size(), list.size()); i++) {
			try {
				if(Objects.equals(role.toLowerCase(), getRoleName(i).toLowerCase()) || Objects.equals(role.toLowerCase(), getRoleTypo(i).toLowerCase())) {
					return true;
				}
			}catch (ArrayIndexOutOfBoundsException | JSONException ignored) {}
		}
		return false;
	}
	
	public static boolean assingableRole(String role, Guild gld) {
		List<Object> list = getRoles().toList();
		for(int i = 0; i < Math.max(gld.getRoles().size(), list.size()); i++) {
			try {
				if(Objects.equals(role.toLowerCase(), getRoleName(i).toLowerCase()) || Objects.equals(role.toLowerCase(), getRoleTypo(i).toLowerCase())) {
					if(isAssignable(i)) {
						return true;
					}
				}
			}catch (ArrayIndexOutOfBoundsException | JSONException ignored) {}
		}
		return false;
	}
}
