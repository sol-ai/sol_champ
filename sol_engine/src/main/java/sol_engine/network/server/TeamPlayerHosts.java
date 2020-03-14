package sol_engine.network.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamPlayerHosts {
    public static class TeamPlayer {
        public int teamIndex, playerIndex;

        public TeamPlayer(int teamIndex, int playerIndex) {
            this.teamIndex = teamIndex;
            this.playerIndex = playerIndex;
        }
    }

    private final Logger logger = LoggerFactory.getLogger(TeamPlayerHosts.class);

    private List<List<Host>> teamPlayerHosts;  // should be of fixed size


    public TeamPlayerHosts(List<Integer> teamSizes) {
        teamPlayerHosts = teamSizes.stream()
                .map(teamSize ->
                        IntStream.range(0, teamSize)
                                .mapToObj(i -> (Host) null)
                                .collect(Collectors.toList())

                )
                .collect(Collectors.toList());
    }

    public TeamPlayerHosts(TeamPlayerHosts toCopy) {
        teamPlayerHosts = toCopy.teamPlayerHosts.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public void setHost(int teamIndex, int playerIndex, Host host) {
        teamPlayerHosts.get(teamIndex).set(playerIndex, host);
    }

    public void setHost(TeamPlayer teamPlayer, Host host) {
        setHost(teamPlayer.teamIndex, teamPlayer.playerIndex, host);
    }

    public Host getHost(int teamIndex, int playerIndex) {
        return teamPlayerHosts.get(teamIndex).get(playerIndex);
    }

    public Host getHost(TeamPlayer teamPlayer) {
        return getHost(teamPlayer.teamIndex, teamPlayer.playerIndex);
    }

    public void replaceHost(Host oldHost, Host newHost) {
        TeamPlayer oldHostTeamPlayer = getTeamPlayer(oldHost);
        setHost(oldHostTeamPlayer, newHost);
    }

    public TeamPlayer getTeamPlayer(Host host) {
        int teamIndex = IntStream.range(0, teamPlayerHosts.size())
                .filter(ti -> teamPlayerHosts.get(ti).contains(host))
                .findFirst()
                .orElse(-1);
        if (teamIndex == -1) {
            return null;
        }
        int playerIndex = IntStream.range(0, teamPlayerHosts.get(teamIndex).size())
                .filter(pi -> teamPlayerHosts.get(teamIndex).get(pi).equals(host))
                .findFirst()
                .orElse(-1);
        if (playerIndex == -1) {
            // should never happen
            return null;
        }
        return new TeamPlayer(teamIndex, playerIndex);
    }

    public boolean checkHostExists(Host host) {
        return getTeamPlayer(host) == null;
    }

    public boolean checkConnectonKeyExists(String connectionKey) {
        return teamPlayerHosts.stream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .anyMatch(host -> host.connectionKey.equals(connectionKey));
    }
}
