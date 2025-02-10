package ru.otus.example.serialization.entitites;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JacksonXmlRootElement(localName = "user_teams")
public class UserTeams {
    //FIXME XML-deserialization does not work
    // when internal object in the list of map values
    // has more that one parameter in the constructor
    @JsonIgnore
    @JacksonXmlElementWrapper(localName = "multi_map", useWrapping = false)
    @JacksonXmlProperty(localName = "list_in_map")
    private Map<String, List<User>> multiMap = new LinkedHashMap<>();

    @JacksonXmlElementWrapper(localName = "map", useWrapping = false)
    @JacksonXmlProperty(localName = "users_in_map")
    private Map<String, User> userMap = new LinkedHashMap<>();

    @JacksonXmlElementWrapper(localName = "multi_list", useWrapping = false)
    @JacksonXmlProperty(localName = "user_lists_in_list")
    private List<UserTeamWrapper> multiList = new LinkedList<>();

    @JacksonXmlElementWrapper(localName = "list", useWrapping = true)
    @JacksonXmlProperty(localName = "users_in_list")
    private List<User> userList = new LinkedList<>();

    private User singleUser;

    public void addEntryToMultiMap(String key, User entry) {
        List<User> map = multiMap.getOrDefault(key, new ArrayList<>());
        map.add(entry);
        multiMap.putIfAbsent(key, map);
    }

    public void addEntryToMap(String key, User entry) {
        userMap.putIfAbsent(key, entry);
    }

    public void addEntryToList(User entry) {
        userList.add(entry);
    }

    public void addEntryToMultiList(User user, String team) {
        multiList.stream()
                .filter(userTeam -> userTeam.getTeam().equals(team))
                .findFirst()
                .ifPresentOrElse(wrapper -> wrapper.getUsers().add(user),
                        () -> {
                            UserTeamWrapper wrapper = new UserTeamWrapper(team);
                            wrapper.getUsers().add(user);
                            multiList.add(wrapper);
                        }
                );
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Builder
    public static class User {
        private String name;
        private String gender;
        private Integer age;

        @JsonCreator
        public User(@JsonProperty("user_name") String name,
                    @JsonProperty("user_age") Integer age,
                    @JsonProperty("user_gender") String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }
    }

    @ToString
    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class UserTeamWrapper {
        private String team;
        private final List<User> users = new LinkedList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserTeams userTeams = (UserTeams) o;
        return Objects.equals(userMap, userTeams.userMap) && Objects.equals(multiList, userTeams.multiList) && Objects.equals(userList, userTeams.userList) && Objects.equals(singleUser, userTeams.singleUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userMap, multiList, userList, singleUser);
    }
}
