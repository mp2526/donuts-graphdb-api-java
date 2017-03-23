/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api;

import com.seedlabs.donuts.api.resource.server.*;
import com.thinkaurelius.titan.core.TitanGraph;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MemoryRealm;
import org.restlet.security.Role;
import org.restlet.security.User;
import org.restlet.service.CorsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GraphApiApplication extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(GraphApiApplication.class);

    /*
     * Define route constants
     */
    public static final String ROUTE_ROOT = "/";

    public static final String ROUTE_ASSETS = "/assets";

    public static final String ROUTE_ASSETSACTIVE = "/assets/active";

    public static final String ROUTE_ASSETSPOPULAR = "/assets/popular";

    public static final String ROUTE_ASSETSRECENT = "/assets/recent";

    public static final String ROUTE_ASSETSSHAREDWITH = "/assets/sharedwith";

    public static final String ROUTE_ASSETSCATEGORIES = "/assets/categories";

    public static final String ROUTE_ASSETSASSETID = "/assets/{assetid}";

    public static final String ROUTE_ASSETSASSETIDCOMMENTS = "/assets/{assetid}/comments";

    public static final String ROUTE_ASSETSASSETIDLIKES = "/assets/{assetid}/likes";

    public static final String ROUTE_GROUPS = "/groups";

    public static final String ROUTE_GROUPSMEMBEROF = "/groups/memberof";

    public static final String ROUTE_GROUPSNEW = "/groups/new";

    public static final String ROUTE_GROUPSPOPULAR = "/groups/popular";

    public static final String ROUTE_GROUPSGROUPID = "/groups/{groupid}";

    public static final String ROUTE_GROUPSGROUPIDFEED = "/groups/{groupid}/feed";

    public static final String ROUTE_USERS = "/users";

    public static final String ROUTE_USERSSELF = "/users/self";

    public static final String ROUTE_USERSSELFFAVORITES = "/users/self/favorites";

    public static final String ROUTE_USERSSELFFEED = "/users/self/feed";

    public static final String ROUTE_USERSSELFFOLLOWEDBY = "/users/self/followedby";

    public static final String ROUTE_USERSSELFFOLLOWS = "/users/self/follows";

    public static final String ROUTE_USERSSELFRECENTITEMS = "/users/self/recentitems";

    public static final String ROUTE_USERSSELFRECOMMENDATIONS = "/users/self/recommendations";

    public static final String ROUTE_USERSSELFSKILLS = "/users/self/skills";

    public static final String ROUTE_USERSUSERID = "/users/{userid}";

    public static final String ROUTE_USERSUSERIDFEED = "/users/{userid}/feed";

    public static final String ROUTE_USERSUSERIDFOLLOWEDBY = "/users/{userid}/followedby";

    public static final String ROUTE_USERSUSERIDFOLLOWS = "/users/{userid}/follows";

    public static final String ROUTE_USERSUSERIDSKILLS = "/users/{userid}/skills";

    public static final String ROUTE_POSTS = "/posts";

    public static final String ROUTE_POSTSPOSTID = "/posts/{postid}";

    public static final String ROUTE_POSTSPOSTIDCOMMENTS = "/posts/{postid}/comments";

    public static final String ROUTE_COMMENTSCOMMENTID = "/comments/{commentid}";

    public static final String ROUTE_EXPORT = "/export";


    /*
     * Define role names
     */
    public static final String ROLE_ADMIN = "admin";

    public static final String ROLE_ANYONE = "anyone";

    public static final String ROLE_DEV = "cellroledev";

    public static final String ROLE_OWNER = "cellroleowner";

    public static final String ROLE_USER = "cellroleuser";

    private String versionFull;

    private int versionMajor;

    private int versionMicro;

    private int versionMinor;

    private TitanGraphSingleton titanGraph;
    
    public GraphApiApplication() {
    	setName("donutsService");
        setDescription("Donuts API RESTful Service");
        setOwner("Seedlabs LLC");
        setAuthor("Mike Perry");

        CorsService corsService = new CorsService();
        corsService.setAllowedCredentials(true);
        corsService.setSkippingResourceForCorsOptions(true);
        getServices().add(corsService);

        configureJacksonConverter();

        try {
            this.titanGraph = TitanGraphSingleton.getInstance();

            this.titanGraph.createGraph("dynamodb.properties");
            DonutsGraphFactory.load(this.titanGraph.getGraph(), false);
        } catch (Exception ex) {
            LOG.error("Error Loading Graph", ex);
        }
    }

	private ChallengeAuthenticator createApiGuard(Restlet next) {

        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_BASIC, "realm");

        // Create in-memory users and roles.
        MemoryRealm realm = new MemoryRealm();
        User owner = new User("owner", "owner");
        realm.getUsers().add(owner);
        realm.map(owner, Role.get(this, ROLE_OWNER));
        realm.map(owner, Role.get(this, ROLE_USER));
        realm.map(owner, Role.get(this, ROLE_DEV));
        User admin = new User("admin", "admin");
        realm.getUsers().add(admin);
        realm.map(admin, Role.get(this, ROLE_ADMIN));
        realm.map(admin, Role.get(this, ROLE_OWNER));
        realm.map(admin, Role.get(this, ROLE_USER));
        realm.map(admin, Role.get(this, ROLE_DEV));
        User user = new User("user", "user");
        realm.getUsers().add(user);
        realm.map(user, Role.get(this, ROLE_USER));

        // Verifier : to check authentication
        apiGuard.setVerifier(realm.getVerifier());
        // Enroler : add authorization roles
        apiGuard.setEnroler(realm.getEnroler());

        // You can also create your own authentication/authorization system by
        // creating classes extending SecretVerifier or LocalVerifier (for
        // authentication) and Enroler (for authorization) and set these to the
        // guard.

        apiGuard.setNext(next);

        // In case of anonymous access supported by the API.
        apiGuard.setOptional(true);

        return apiGuard;
    }
    
    public Router createApiRouter() {
        Router apiRouter = new Router(getContext());
        apiRouter.attach(ROUTE_ROOT, RootServerResource.class);
        apiRouter.attach(ROUTE_ASSETS, AssetsServerResource.class);
        apiRouter.attach(ROUTE_ASSETSACTIVE, AssetsActiveServerResource.class);
        apiRouter.attach(ROUTE_ASSETSPOPULAR, AssetsPopularServerResource.class);
        apiRouter.attach(ROUTE_ASSETSRECENT, AssetsRecentServerResource.class);
        apiRouter.attach(ROUTE_ASSETSSHAREDWITH, AssetsSharedWithServerResource.class);
        apiRouter.attach(ROUTE_ASSETSCATEGORIES, AssetsCategoriesServerResource.class);
        apiRouter.attach(ROUTE_ASSETSASSETID, AssetsAssetidServerResource.class);
        apiRouter.attach(ROUTE_ASSETSASSETIDCOMMENTS, AssetsAssetidCommentsServerResource.class);
        apiRouter.attach(ROUTE_ASSETSASSETIDLIKES, AssetsAssetidLikesServerResource.class);
        apiRouter.attach(ROUTE_GROUPS, GroupsServerResource.class);
        apiRouter.attach(ROUTE_GROUPSMEMBEROF, GroupsMembersServerResource.class);
        apiRouter.attach(ROUTE_GROUPSPOPULAR, GroupsPopularServerResource.class);
        apiRouter.attach(ROUTE_GROUPSNEW, GroupsNewServerResource.class);
        apiRouter.attach(ROUTE_GROUPSGROUPID, GroupsGroupidServerResource.class);
        apiRouter.attach(ROUTE_GROUPSGROUPIDFEED, GroupsGroupidFeedServerResource.class);
        apiRouter.attach(ROUTE_USERS, UsersServerResource.class);
        apiRouter.attach(ROUTE_USERSSELF, UsersSelfServerResource.class);
        apiRouter.attach(ROUTE_USERSSELFFAVORITES, UsersSelfFavoritesServerResource.class);
        apiRouter.attach(ROUTE_USERSSELFFEED, UsersSelfFeedServerResource.class);
        apiRouter.attach(ROUTE_USERSSELFFOLLOWEDBY, UsersSelfFollowedbyServerResource.class);
        apiRouter.attach(ROUTE_USERSSELFFOLLOWS, UsersSelfFollowsServerResource.class);
        apiRouter.attach(ROUTE_USERSSELFRECENTITEMS, UsersSelfRecentItemsServerResource.class);
        apiRouter.attach(ROUTE_USERSSELFRECOMMENDATIONS, UsersSelfRecommendationsServerResource.class);
        apiRouter.attach(ROUTE_USERSSELFSKILLS, UsersSelfSkillsServerResource.class);
        apiRouter.attach(ROUTE_USERSUSERID, UsersUseridServerResource.class);
        apiRouter.attach(ROUTE_USERSUSERIDFEED, UsersUseridFeedServerResource.class);
        apiRouter.attach(ROUTE_USERSUSERIDFOLLOWEDBY, UsersUseridFollowedbyServerResource.class);
        apiRouter.attach(ROUTE_USERSUSERIDFOLLOWS, UsersUseridFollowsServerResource.class);
        apiRouter.attach(ROUTE_USERSUSERIDSKILLS, UsersUseridSkillsServerResource.class);
        apiRouter.attach(ROUTE_POSTS, PostsServerResource.class);
        apiRouter.attach(ROUTE_POSTSPOSTID, PostsPostidServerResource.class);
        apiRouter.attach(ROUTE_POSTSPOSTIDCOMMENTS, PostsPostidCommentsServerResource.class);
        apiRouter.attach(ROUTE_COMMENTSCOMMENTID, CommentsCommentidServerResource.class);
        apiRouter.attach(ROUTE_EXPORT, ExportServerResource.class);

        return apiRouter;
	}

    public Restlet createInboundRoot() {

        // Router for the API's resources
        Router apiRouter = createApiRouter();
        // Protect the set of resources
        ChallengeAuthenticator guard = createApiGuard(apiRouter);

        return guard;
    }

    public TitanGraph getTitanGraph() {
        return this.titanGraph.getGraph();
    }

    private void configureJacksonConverter() {
        JacksonConverter jacksonConverter = getRegisteredJacksonConverter();

        if (jacksonConverter != null) {
            Engine.getInstance().getRegisteredConverters().remove(jacksonConverter);
            SeedJacksonConverter converter = new SeedJacksonConverter();
            Engine.getInstance().getRegisteredConverters().add(converter);
        }
    }

    private JacksonConverter getRegisteredJacksonConverter() {
        JacksonConverter jacksonConverter = null;
        List<ConverterHelper> converters = Engine.getInstance().getRegisteredConverters();

        for (ConverterHelper converterHelper : converters) {
            if (converterHelper instanceof JacksonConverter) {
                jacksonConverter = (JacksonConverter) converterHelper;
                break;
            }
        }

        return jacksonConverter;
    }
}
