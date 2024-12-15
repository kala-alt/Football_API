package com.example.demo.Controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entities.AccountEntity;
import com.example.demo.Entities.EventEntity;
import com.example.demo.Entities.LeagueEntity;
import com.example.demo.Entities.MatchEntity;
import com.example.demo.Entities.PlayerEntity;
import com.example.demo.Entities.TeamEntity;
import com.example.demo.Services.AccountService;
import com.example.demo.Services.EmailService;
import com.example.demo.Services.LeagueService;
import com.example.demo.Services.MatchService;
import com.example.demo.Services.PlayerService;
import com.example.demo.Services.TeamService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("accountModel")
public class MainController implements ErrorController {

    //Some declarations
    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private AccountService accountService;

    @Autowired 
    private EmailService emailService;

    @Autowired
    private MongoOperations mongoOperations;

    private int secutiryCode;

    @SuppressWarnings("null")
    @ModelAttribute
    public void setLoggedUser(Model model, HttpServletRequest request) {
        try{
            AccountEntity accountModel = (AccountEntity) model.getAttribute("accountModel");

            model.addAttribute("loggedUsername", accountModel.getUsername());
            model.addAttribute("userAdmin", accountModel.getIsAdmin());

            System.out.println("\n*******\nsetLoggedUser  username: " + accountModel.getUsername() + "       userAdmin: " 
            + accountModel.getIsAdmin());
        }catch(Exception e){
            System.err.println("There is no logged user yet!");
        }
    }

    @RequestMapping("/error")
    public String handleError() {
        return "404.html";
    }
    
    @GetMapping("/sendCorrespondenceEmails")
    public void sendCorrespondenceEmails(@RequestParam(required = true) String Title, @RequestParam(required = true) String MessageContent, @RequestParam(required = true) String SendTo) {

        emailService.sendSimpleEmail("kalaalt19@gmail.com", "Sent to " + SendTo + " : " + Title, MessageContent);

        switch (SendTo) {
            case "Admins":
                for(AccountEntity admin : accountService.findAllAdmins())
                    emailService.sendSimpleEmail(admin.getEmail(), "Sent to " + SendTo + " : " + Title, MessageContent);
                break;

            case "All Users":
                for(AccountEntity users : accountService.findAllAccounts())
                    if(users.getIsAdmin())
                        emailService.sendSimpleEmail(users.getEmail(), "Sent to " + SendTo + " : " + Title, MessageContent);
                    else
                        emailService.sendSimpleEmail(users.getEmail(), Title, MessageContent);
                break;
        }
    }

    @PostMapping("/sendContactEmail")
    public String sendContactEmail(Model model, @RequestParam String Title, @RequestParam String MessageContent, @RequestParam String userEmail, @RequestParam String userId) throws Exception {
            emailService.sendSimpleEmailContact("kalaalt19@gmail.com", Title, MessageContent, userEmail, userId);
            return showEvents(model, null);
    }


    //Make user to be admin
    @PostMapping("/requestNewAdmin")
    public String makeAdmin(Model model, @RequestParam("userEmail") String userEmail){

        System.out.println("\n*************************\n\nWe are sending email...\n");
        emailService.sendSimpleEmail("kalaalt19@gmail.com", "New Admin Request!", "User wants to be promoted as admin! Click this link or "
        + "\npaste it in your browser to approve him: http://localhost:8080/approvedNewAdmin?userEmail=" + userEmail + "&authorityPassword=" + "zdzf2rt365!g@a19t");

        model.addAttribute("accountModel", accountService.findUserByEmail(userEmail));
        
        return "MyAccount.html";
    }

    //Admin approve user`s promotion via opening link from the admin`s inbox
    @GetMapping("/approvedNewAdmin")
    public String approvedNewAdmin(@RequestParam("userEmail") String userEmail, @RequestParam(value = "authorityPassword", required = true) String authorityPassword) {

        if(authorityPassword.equals("zdzf2rt365!g@a19t")){
            mongoOperations.updateFirst(
            new Query(Criteria.where("email").is(userEmail)),
            new Update().set("isAdmin", true), 
            AccountEntity.class);

            emailService.sendSimpleEmail(userEmail, "Football_API: Welcome to the admin team!", "Congrats, you have been successfully promoted to admin!");
        }

        return "Admin.html";
    }

    //Login and Register for users/admins
    @GetMapping("/login")
    public String showLoginFormGetMapping(Model model, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        model.addAttribute("loggedUsername", null);
        model.addAttribute("loginStat", "false");
        return "Login.html";
    }

    @PostMapping(value = "/logged", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String showSuccessLog(Model model, @RequestParam("username") String username, @RequestParam("password") String password ) throws Exception {
            AccountEntity accountModel = accountService.findLoginUser(username, password);

        try{
            accountService.setNewLastLogin(accountModel.getId());
            System.out.println("\n\n****************************\nIs admin: " + accountModel.getIsAdmin() + "  username: " +  accountModel.getUsername());
        }catch(Exception e){
            System.err.println("***Inccorect username or password!!!");
            model.addAttribute("loginStat", "true");
            return "Login.html";
        }

        // emailService.sendSimpleEmail(accountModel.getEmail(), "Successfully registration!", "Nice to see you in our Football API! Feel free to use our services!");

        model.addAttribute("loggedUsername", username);
        model.addAttribute("userAdmin", accountModel.getIsAdmin());

        model.addAttribute("accountModel", accountModel);
        return "MyAccount.html";
    }

    @GetMapping("/myAccount")
    public String showMyAccount(Model model) {
        model.addAttribute("accountModel", (AccountEntity) model.getAttribute("accountModel"));
        return "MyAccount.html";
    }

    @GetMapping("/adminActions")
    public String showAdminActions() {
        return "AdminActions.html";
    }
    
    
    @PostMapping("/registered")
    public String showSuccessReg(Model model, @ModelAttribute("accountModel") AccountEntity accountModel) {

        if(accountService.findUserByUsername(accountModel.getUsername()) != null) {
            model.addAttribute("usedUsername", "true");
            return "Register.html";
        }

        accountService.saveAccount(accountModel);
        String id = accountModel.getId().substring(0, accountModel.getId().length()-2);

        String rndNums = "a0b2c1d3e4f5g7h6i8j9klmnopqrst";
        Random rnd = new Random();

        for(int i=0; i<2; i++){
            int randomNum = rnd.nextInt(rndNums.length());
            id+=rndNums.charAt(randomNum);
            System.out.println("rnd value: " + randomNum + "   text value: " + rndNums.charAt(randomNum) + "\n New id value: " + id);
        }

        accountModel.setApi_key(id);
        accountService.saveAccount(accountModel);
        model.addAttribute("accountModel", new AccountEntity());
        return "Login.html";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usedUsername", "false");
        model.addAttribute("accountModel", new AccountEntity());
        return "Register.html";
    }


    @PostMapping("/Retire")
    public String RetirePlayer(Model model, @RequestParam String playerId){
        mongoOperations.updateFirst(
            new Query(Criteria.where("_id").is(playerId)),
            new Update().set("retired", !playerService.findPlayerViaId(playerId).isRetired()), 
            PlayerEntity.class);

            model.addAttribute("plTeams", teamService.getAllTeams());
        return "SearchPage.html";
    }

    @PostMapping("/Transfer")
    public String TransferPlayer(Model model, @RequestParam String playerId, @RequestParam String newTeam){
        mongoOperations.updateFirst(
            new Query(Criteria.where("_id").is(playerId)),
            new Update().set("team", newTeam), 
            PlayerEntity.class);
            model.addAttribute("plTeams", teamService.getAllTeams());
        return "SearchPage.html";
    }


    @PostMapping("/LeagueEdit")
    public String showLeagueEdit(){

        return "LeagueEdit.html";
    }

    @PostMapping("/TeamEdit")
    public String showTeamEdit(){

        return "TeamEdit.html";
    }

    
    @GetMapping("/VerifiedActions")
    public String showVerifiedActions() {
        return "VerifiedActions.html";
    }

    //---------------------------------------------------------------------------------


    @GetMapping("/CorrespondenceEmails")
    public String CorrespondenceEmails() {
        return "Communication.html";
    }

    @GetMapping("/search")
    public String showSearchPage(Model model) {
        model.addAttribute("plTeams", teamService.getAllTeams());
        return "SearchPage.html";
    }

    @SuppressWarnings("null")
    @GetMapping("/ContactUs")
    public String showContactUs(Model model) {
        AccountEntity acc = (AccountEntity) model.getAttribute("accountModel");
        try{
            System.out.println("User Email: " + acc.getEmail());
            model.addAttribute("userEmail", ((AccountEntity) model.getAttribute("accountModel")).getEmail());
            model.addAttribute("userId", ((AccountEntity) model.getAttribute("accountModel")).getId());
        }catch(Exception e){
            model.addAttribute("userEmail", null);
        }
        return "ContactUs.html";
    }
    

    @PostMapping("/searchResults")
    public String postMethodName(Model model, @RequestParam(required = true) String searchBar) {
        model.addAttribute("leagues", leagueService.findLeague(searchBar));
        model.addAttribute("players", playerService.findPlayerViaName(searchBar));
        model.addAttribute("teams", teamService.findTeamViaName(searchBar));
        model.addAttribute("plTeams", teamService.getAllTeams());
        
        return "SearchPage.html";
    }
    
    
    //Update Query counter
    public void processedUserQuery(String api_key){
        mongoOperations.updateFirst(
        new Query(Criteria.where("api_key").is(api_key)),
        new Update().set("queriesThisMonth", 
        accountService.findUserByApiKey(api_key).getQueriesThisMonth()+1), 
        AccountEntity.class);
    }

    @GetMapping("/showQueries")
    public String showAllQueries() {
        return "ShowQueryStructure.html";
    }
    

    //JSON Data
    @GetMapping("/matchesData")
    @ResponseBody
    public List<MatchEntity> getAllMatchesData(Model model, @RequestParam(required = true) String api_key) {
        if(accountService.findUserByApiKey(api_key).getQueriesThisMonth() < 100){
            processedUserQuery(api_key);
            return matchService.getAllMatches();
        }else return null;
    }
    
    @GetMapping("/playersData")
    @ResponseBody
    public List<PlayerEntity> getAllPlayersData(Model model, @RequestParam(required = true) String api_key) {
        if(accountService.findUserByApiKey(api_key).getQueriesThisMonth() < 100){
            processedUserQuery(api_key);
            return playerService.getAllPlayers();
        }else return null;
    }

    @GetMapping("/teamsData")
    @ResponseBody
    public List<TeamEntity> getAllTeamsData(Model model, @RequestParam(required = true) String api_key) {
        if(accountService.findUserByApiKey(api_key).getQueriesThisMonth() < 100){
            processedUserQuery(api_key);
            return teamService.getAllTeams();
        }else return null;
    }
    
    @GetMapping("/LeaguesData")
    @ResponseBody
    public List<LeagueEntity> getAllLeaguesData(Model model, @RequestParam(required = true) String api_key) {
        if(accountService.findUserByApiKey(api_key).getQueriesThisMonth() < 100){
            processedUserQuery(api_key);
            return leagueService.getAllLeagues();
        }else return null;
    }


    //Admin controller`s methods
    @GetMapping("/allEvents")
    public String showEvents(Model model, String date) throws Exception {

        System.err.println("\n\n***********************\nGet mapping of /allEvents is now running...  date:" + date + "\n\n");

        if (date!=null) {
            
            Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);

            model.addAttribute("events", matchService.getAllMatchesViaDate(new SimpleDateFormat("dd.MM.yyyy").format(dt)));
            model.addAttribute("selected_date", date);
        }else{
            Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(LocalDate.now()));
            model.addAttribute("events", matchService.getAllMatchesViaDate(String.valueOf(new SimpleDateFormat("dd.MM.yyyy").format(dt))));
            model.addAttribute("selected_date", LocalDate.now());
        }

        model.addAttribute("matchService", matchService);
        model.addAttribute("teamService", teamService);

        return "AllEvents.html";
    }


    @PostMapping("/allEvents")
    public void showEventsAfterFinishMatch(Model model, 
     @RequestParam(name = "id", required = false) String matchId,
     @RequestParam(name = "finished", required = false) Boolean finished,
     @RequestParam(name = "date", required = false) String date,
     @ModelAttribute("playerEntity") PlayerEntity playerEntity,
     @ModelAttribute("matchEntity") MatchEntity matchEntity,
     @ModelAttribute("leagueEntity") LeagueEntity leagueEntity,
     @ModelAttribute("teamEntity") TeamEntity teamEntity,
     @RequestParam(name = "pictureFile", required = false) MultipartFile pictureFile,
     @RequestParam(name = "countryFlag", required = false) MultipartFile countryFlag,
     @RequestParam (name = "type", required = true) String type) throws Exception {

        System.err.println("\n\n\n\n\n\n************showEventsAfterFinishMatch() is now working......  type: " + type + "      date: " + date);

        switch (type) {
            case "addMatch":
                matchEntity.setFinished(false);
                Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(matchEntity.getDate());
                matchEntity.setDate(new SimpleDateFormat("dd.MM.yyyy").format(dt));

                if(matchEntity.getHome_team().equals(matchEntity.getAway_team()))
                    showAddMatch(model, "Invalid match!");
                else 
                if(matchService.checkAlreadyAddedMatch(matchEntity.getHome_teamId(), matchEntity.getAway_teamId(), matchEntity.getLeagueId(), matchEntity.getRound()) == null ||
                    matchService.checkAlreadyAddedMatch(matchEntity.getHome_teamId(), matchEntity.getAway_teamId(), matchEntity.getLeagueId(), matchEntity.getRound()) == true){

                    matchService.saveMatch(matchEntity);
                    showEvents(model, date);
                }else
                    showAddMatch(model, "This match already exists!");
            break;
        
            case "finished" :
                MatchEntity match = matchService.findMatchById(matchId);
                
                if(finished != null)
                    match.setFinished(!finished);

                matchService.saveMatch(match);

                //Works

                TeamEntity homeTeam = teamService.findTeamById(match.getHome_teamId()), awayTeam = teamService.findTeamById(match.getAway_teamId());

                // Update Home team stats
                Update homeTeamUpdate = new Update()
                    .set("scoredGoals", homeTeam.getScoredGoals() + match.numOfHome_goals())
                    .set("concededGoals", homeTeam.getConcededGoals() + match.numOfAway_goals())
                    .set("NumYellowCards", homeTeam.getNumYellowCards() + match.numOfHomeTeamYellow_cards())
                    .set("NumRedCards", homeTeam.getNumRedCards() + match.numOfHomeTeamRed_cards());

                // Update Away team stats
                Update awayTeamUpdate = new Update()
                    .set("scoredGoals", awayTeam.getScoredGoals() + match.numOfAway_goals())
                    .set("concededGoals", awayTeam.getConcededGoals() + match.numOfHome_goals())
                    .set("NumYellowCards", awayTeam.getNumYellowCards() + match.numOfAwayTeamYellow_cards())
                    .set("NumRedCards", awayTeam.getNumRedCards() + match.numOfAwayTeamRed_cards());

                // Determine if it's a win, loss, or draw
                if (match.numOfHome_goals() > match.numOfAway_goals()) {
                    homeTeamUpdate.set("wins", homeTeam.getWins() + 1);
                    awayTeamUpdate.set("losses", awayTeam.getLosses() + 1);
                } else 
                if (match.numOfHome_goals() < match.numOfAway_goals()) {
                    homeTeamUpdate.set("losses", homeTeam.getLosses() + 1);
                    awayTeamUpdate.set("wins", awayTeam.getWins() + 1);
                } else {
                    homeTeamUpdate.set("draws", homeTeam.getDraws() + 1);
                    awayTeamUpdate.set("draws", awayTeam.getDraws() + 1);
                }

                // Execute the updates
                mongoOperations.updateFirst(new Query(Criteria.where("_id").is(match.getHome_teamId())), homeTeamUpdate, TeamEntity.class);
                mongoOperations.updateFirst(new Query(Criteria.where("_id").is(match.getAway_teamId())), awayTeamUpdate, TeamEntity.class);

                //Works
                teamService.updatePointsAndStanding(match.getLeagueId());

                showEvents(model, date);
            break;
            
            case "addPlayer" :
                playerEntity.setPicture(pictureFile.getBytes());

                // new SimpleDateFormat("dd.MM.yyyy").format(playerEntity.getDate_Of_Birth());

                playerService.savePlayer(playerEntity);
                showEvents(model, date);
            break;

            case "addTeam" :

                if(leagueService.numberOfLeagueTeams() == leagueService.findLeagueById(teamEntity.getLeague_id()).getNumberOfTeams())
                    addTeam(model, "League is full of teams!");
                else{       
                    teamEntity.setLogo_img(pictureFile.getBytes());
                    teamService.saveTeam(teamEntity);
                    showEvents(model, date);
                }
            break;
                

            case "addLeague" :
                System.out.println("\n\n\n**********************\nTestttt: " + pictureFile.getBytes() 
                + "\n countryFlag: " + countryFlag.getBytes());
                    leagueEntity.setLogoImg(pictureFile.getBytes());
                    leagueEntity.setCountryFlag(countryFlag.getBytes());
                    leagueService.saveLeague(leagueEntity);
                    showEvents(model, date);
                break;

            case "NewDate":

            // Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(matchEntity.getDate());
            // matchEntity.setDate(new SimpleDateFormat("dd.MM.yyyy").format(dt));


                Date searchDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                showEvents(model, new SimpleDateFormat("dd.MM.yyyy").format(searchDate));
            break;

            default: System.err.println("Problem in Main Controller with type of actions!");
                break;
        }
    }

    
    @SuppressWarnings("null")
    @GetMapping("/matchDetails")
    public String postMethodName(
        @RequestParam(name = "type", required = false) String type,
        @RequestParam(name = "id", required = false) String matchId,
        @RequestParam(name = "minute", required = false) Integer minute,
        @RequestParam(name = "player", required = false) String player,   
        @RequestParam(required = false) String playerId, 
        Model model) throws Exception {
                MatchEntity matchEntity = matchService.findMatchById(matchId);

                System.out.println("\n\n***********************\n PlayerId= " + playerId);

            if (type != null){
                 Update update;

                switch (type) {
                    case "goal":
                        matchEntity.getGoals().add(new EventEntity(player, type, minute, playerId, playerService.findPlayerViaId(playerId).getTeam()));
                        update = new Update().set("numbeOfGoals", playerService.findPlayerViaId(playerId).getNumbeOfGoals()+1);
                        break;

                    case "yellow_card":
                        matchEntity.getYellow_cards().add(new EventEntity(player, type, minute, playerId, playerService.findPlayerViaId(playerId).getTeam()));
                        update = new Update().set("yellowCards", playerService.findPlayerViaId(playerId).getYellowCards()+1);
                        break;

                    case "red_card":
                        matchEntity.getRed_cards().add(new EventEntity(player, type, minute, playerId, playerService.findPlayerViaId(playerId).getTeam()));
                        update = new Update().set("redCards", playerService.findPlayerViaId(playerId).getRedCards()+1);
                        break;

                    default: System.err.println("Problem in switch operator!");
                    throw new Exception("Your type parameter is invalid!");
                }
                
                matchService.saveMatch(matchEntity);

                mongoOperations.updateFirst(
                    new Query(Criteria.where("_id").is(playerId)), 
                    update,
                PlayerEntity.class);

            }

                List <EventEntity> list = matchEntity.getGoals();
                Collections.sort(list, (e1, e2) -> Integer.compare(e1.getMinute(),(e2.getMinute())));
                matchEntity.setGoals(list);

                list = matchEntity.getYellow_cards();
                Collections.sort(list, (e1, e2) -> Integer.compare(e1.getMinute(),(e2.getMinute())));
                matchEntity.setYellow_cards(list);

                list = matchEntity.getRed_cards();
                Collections.sort(list, (e1, e2) -> Integer.compare(e1.getMinute(),(e2.getMinute())));
                matchEntity.setRed_cards(list);

                model.addAttribute("match", matchEntity);
                try{
                    model.addAttribute("userAdmin", ((AccountEntity) model.getAttribute("accountModel")).getIsAdmin());
                }catch(Exception e){
                    System.err.println("No logged user!");
                }

                model.addAttribute("homeTeamLogo", matchService.getTeamLogoAsBase64(
                    teamService.findTeamViaName(matchEntity.getHome_team()).get(0).getLogo_img()                
                ));
                model.addAttribute("awayTeamLogo", matchService.getTeamLogoAsBase64(
                    teamService.findTeamViaName(matchEntity.getAway_team()).get(0).getLogo_img()                
                ));
                return "MatchDetails.html";
    }


    @GetMapping("/AddAction")
    public String showAddAction(@RequestParam(name = "id", required = true) String matchId, Model model) {


        List <PlayerEntity> players = new ArrayList<PlayerEntity>(
            playerService.getMatchPlayers(
                matchService.findMatchById(matchId).getHome_team(),
                matchService.findMatchById(matchId).getAway_team()));

        //Test players list
        System.out.println("\n\n\n Players list test: " + players);
        
        model.addAttribute("players", players);
        model.addAttribute("matchId", matchId);
        model.addAttribute("eventEntity", new EventEntity());
        
        return "AddAction.html";
    }

    
    //Works perfectly
    @GetMapping("/addMatch")
    public String showAddMatch(Model model, String message) {
        model.addAttribute("matchEntity", new MatchEntity());
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("leagues", leagueService.getAllLeagues());
        model.addAttribute("message", message);

        return "AddMatches.html";
    }

    @GetMapping("/addLeague")
    public String addLeague(Model model) {
        model.addAttribute("leagueEntity", new LeagueEntity());

        return "AddLeague.html";
    }

    //Works perfectly
    @GetMapping("/addTeam")
    public String addTeam(Model model, String message) { 
        model.addAttribute("teamEntity", new TeamEntity());
        model.addAttribute("message", message);
        System.out.println("Leagues: " + leagueService.getAllLeagues());
        model.addAttribute("leagues", leagueService.getAllLeagues());

        return "AddTeam.html";
    }
    
    //Fixed and Works perfectly
    @GetMapping("/addPlayer")
    public String addPlayer(Model model) {
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("playerEntity", new PlayerEntity());

        return "AddPlayer.html";
    }



// --------------------------------------------------------------------------------------------------------------------------------------------------

    public void sendVerificationCode(){
        Random rnd = new Random();
    
        do{
            secutiryCode = rnd.nextInt(10000);
        }while(secutiryCode<=999);

        System.out.println("Secutiry code value: " + secutiryCode);
        emailService.sendSimpleEmail("kdimitrov19@abv.bg", "Verify to Null all user`s month queries!", "Your verification code is: " + secutiryCode);
    }


    @GetMapping("/nullMonthQueries")
    public String midNullMonthQueries(Model model){
        
        sendVerificationCode();
        model.addAttribute("type", "setNullMonthQueries");

        return "VerifyCode.html";
    }

    @GetMapping("/midRestartSeason")
    public String midRestartSeason(Model model) {
        sendVerificationCode();
        model.addAttribute("type", "restartSeason");

        return "VerifyCode.html";
    }

    @GetMapping("/setNullMonthQueries")
    public String nullMonthQueries(Model model, @RequestParam int inputCode) throws Exception{
        System.out.println("We are in checkVerifyCode()");

        if(inputCode == secutiryCode){
            for(AccountEntity account : accountService.findAllAccounts()){
                mongoOperations.updateFirst(
                    new Query(Criteria.where("_id").is(account.getId())), 
                    new Update().set("queriesThisMonth", 0),
                AccountEntity.class);
            }

            showEvents(model, null);
            return "allEvents.html";
        }
        else
            return "VerifiedActions.html";
    }

    @GetMapping("/restartSeason")
    public String restartSeason(Model model, @RequestParam int inputCode) throws Exception{

        if(inputCode == secutiryCode){

            for(TeamEntity team : teamService.getAllTeams()){
                mongoOperations.updateFirst(
                        new Query(Criteria.where("_id").is(team.getId())), 
                        new Update().set("concededGoals", 0)
                        .set("playedMatches", 0)
                        .set("NumRedCards", 0)
                        .set("NumYellowCards", 0)
                        .set("scoredGoals", 0)
                        .set("standing", 0)
                        .set("wins", 0)
                        .set("draws", 0)
                        .set("losses", 0)
                        .set("points", 0),
                    TeamEntity.class);
            }

            for(PlayerEntity player : playerService.getAllPlayers()){
                mongoOperations.updateFirst(
                    new Query(Criteria.where("_id").is(player.getPlayerId())), 
                    new Update().set("numbeOfGoals", 0)
                    .set("yellowCards", 0)
                    .set("redCards", 0),
                PlayerEntity.class);
            }

            showEvents(model, null);
            return "allEvents.html";
        }else
            return "VerifiedActions.html";
    }

}