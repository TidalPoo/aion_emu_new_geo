/*
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.cardinal.Copy;
import com.aionemu.gameserver.cardinal.ItemLog;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.controllers.FlyController;
import com.aionemu.gameserver.controllers.PlayerController;
import com.aionemu.gameserver.controllers.attack.AggroList;
import com.aionemu.gameserver.controllers.attack.AttackStatus;
import com.aionemu.gameserver.controllers.attack.PlayerAggroList;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.controllers.movement.PlayerMoveController;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.dao.MySQL5AionDAO;
import com.aionemu.gameserver.dao.PlayerVarsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.CreatureType;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.WorldBuff;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.Servant;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.SummonedObject;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.AbyssRank.AbyssRankUpdateType;
import com.aionemu.gameserver.model.gameobjects.player.FriendList.Status;
import com.aionemu.gameserver.model.gameobjects.player.emotion.EmotionList;
import com.aionemu.gameserver.model.gameobjects.player.motion.MotionList;
import com.aionemu.gameserver.model.gameobjects.player.npcFaction.NpcFactions;
import com.aionemu.gameserver.model.gameobjects.player.title.TitleList;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseRegistry;
import com.aionemu.gameserver.model.house.HouseStatus;
import com.aionemu.gameserver.model.ingameshop.InGameShop;
import com.aionemu.gameserver.model.items.ItemCooldown;
import com.aionemu.gameserver.model.items.storage.IStorage;
import com.aionemu.gameserver.model.items.storage.LegionStorageProxy;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.skill.PlayerSkillList;
import com.aionemu.gameserver.model.stats.container.PlayerGameStats;
import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.team.legion.LegionMember;
import com.aionemu.gameserver.model.team2.TeamMember;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceGroup;
import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.model.templates.event.MaxCountOfDay;
import com.aionemu.gameserver.model.templates.flypath.FlyPathEntry;
import com.aionemu.gameserver.model.templates.item.ItemAttackType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.templates.ride.RideInfo;
import com.aionemu.gameserver.model.templates.stats.PlayerStatsTemplate;
import com.aionemu.gameserver.model.templates.windstreams.WindstreamPath;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.AccessLevelEnum;
import com.aionemu.gameserver.services.DisputeLandService;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.MembershipLevelEnum;
import com.aionemu.gameserver.services.custom.MixFight;
import com.aionemu.gameserver.services.custom.Prison;
import com.aionemu.gameserver.services.custom.ffa.FfaGroupService;
import com.aionemu.gameserver.services.custom.ffa.FfaLegionService;
import com.aionemu.gameserver.services.drop.PlayerDropItem;
import com.aionemu.gameserver.services.event.WellcomeToAtrea;
import com.aionemu.gameserver.services.serialkillers.SerialKiller;
import com.aionemu.gameserver.services.weddings.Wedding;
import com.aionemu.gameserver.skillengine.condition.ChainCondition;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.effect.EffectTemplate;
import com.aionemu.gameserver.skillengine.effect.RebirthEffect;
import com.aionemu.gameserver.skillengine.effect.ResurrectBaseEffect;
import com.aionemu.gameserver.skillengine.model.ChainSkills;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.skillengine.task.CraftingTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.rates.Rates;
import com.aionemu.gameserver.utils.rates.RegularRates;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * This class is representing Player object, it contains all needed data.
 *
 * @author -Nemesiss-
 * @author SoulKeeper
 * @author alexa026
 * @author cura
 * @modified Alex
 */
public class Player extends Creature {

    public RideInfo ride;
    public InRoll inRoll;
    public InGameShop inGameShop;
    public WindstreamPath windstreamPath;
    private PlayerAppearance playerAppearance;
    private PlayerAppearance savedPlayerAppearance;
    private final PlayerCommonData playerCommonData;
    private final Account playerAccount;
    private LegionMember legionMember;

    private MacroList macroList;
    private PlayerSkillList skillList;
    private FriendList friendList;
    private RequestFriendList requestFriendList;
    private BlockList blockList;
    private PetList toyPetList;
    private Mailbox mailbox;
    private PrivateStore store;
    private TitleList titleList;
    private QuestStateList questStateList;
    private RecipeList recipeList;
    private List<House> houses;

    private ResponseRequester requester;
    private boolean lookingForGroup = false;
    private Storage inventory;
    private final Storage[] petBag = new Storage[StorageType.PET_BAG_MAX - StorageType.PET_BAG_MIN + 1];
    private final Storage[] cabinets = new Storage[StorageType.HOUSE_WH_MAX - StorageType.HOUSE_WH_MIN + 1];
    private Storage regularWarehouse;
    private Storage accountWarehouse;
    private Equipment equipment;
    private HouseRegistry houseRegistry;

    private PlayerStatsTemplate playerStatsTemplate;
    private final AbsoluteStatOwner absStatsHolder;
    private PlayerSettings playerSettings;

    private com.aionemu.gameserver.model.team2.group.PlayerGroup playerGroup2;
    private PlayerAllianceGroup playerAllianceGroup;

    private AbyssRank abyssRank;
    private NpcFactions npcFactions;
    private Rates rates;
    private int flyState = 0;
    private boolean isTrading;
    private long prisonTimer = 0;
    private long startPrison;
    private boolean invul;
    private FlyController flyController;
    private CraftingTask craftingTask;
    private int flightTeleportId;
    private int flightDistance;
    private Summon summon;
    private SummonedObject<?> summonedObj;
    private Pet toyPet;
    private Kisk kisk;
    private boolean isResByPlayer = false;
    private int resurrectionSkill = 0;
    private boolean isFlyingBeforeDeath = false;
    private boolean edit_mode = false;
    private Npc postman = null;
    private boolean isInResurrectPosState = false;
    private float resPosX = 0;
    private float resPosY = 0;
    private float resPosZ = 0;
    private boolean underNoFPConsum = false;
    private boolean isAdminTeleportation = false;
    private boolean cooldownZero = false;
    private boolean isUnderInvulnerableWing = false;
    private boolean isFlying = false;
    private boolean isWispable = true;

    private int abyssRankListUpdateMask = 0;

    private BindPointPosition bindPoint;

    private Map<Integer, ItemCooldown> itemCoolDowns;
    private PortalCooldownList portalCooldownList;
    private CraftCooldownList craftCooldownList;
    private HouseObjectCooldownList houseObjectCooldownList;
    private long nextSkillUse;
    private long nextSummonSkillUse;
    private ChainSkills chainSkills;
    private final Map<AttackStatus, Long> lastCounterSkill = new EnumMap<>(AttackStatus.class);

    private int dualEffectValue = 0;
    /**
     * Static information for players
     */
    private boolean isAttackMode = false;

    private long gatherableTimer = 0;
    private long stopGatherable;
    private String captchaWord;
    private byte[] captchaImage;

    private float instanceStartPosX, instanceStartPosY, instanceStartPosZ;
    private int rebirthResurrectPercent = 1;
    private int rebirthSkill = 0;

    /**
     * Connection of this Player.
     */
    private AionConnection clientConnection;
    private FlyPathEntry flyLocationId;
    private long flyStartTime;

    private EmotionList emotions;
    private MotionList motions;

    private long flyReuseTime;

    private boolean isMentor;

    private long lastMsgTime = 0;
    private int floodMsgCount = 0;

    private long onlineTime = 0;

    private int lootingNpcOid;
    private boolean rebirthRevive;

    // Needed to remove supplements queue
    private int subtractedSupplementsCount;
    private int subtractedSupplementId;
    private int portAnimation;
    private boolean isInSprintMode;
    private List<ActionObserver> rideObservers;
    private SerialKiller skList;

    byte buildingOwnerStates = PlayerHouseOwnerFlags.BUY_STUDIO_ALLOWED.getId();
    private int battleReturnMap;
    private float[] battleReturnCoords;
    private FastList<WorldBuff> worldBuff;
    private int useAutoGroup = 0;
    private int removeSkillDelay = 0;
    private final FastMap<Integer, Object> tempStorage = FastMap.newInstance();
    //new
    private PlayerBonusTime bonusTime;
    public int useskilldebuff;
    private int owc;
    private long owt;
    private long GAGTimer = 0;
    private long startGAG;
    private TeleportTask teleportTask;
    private Map<Integer, MaxCountOfDay> maxCountEvent;
    private boolean tp;
    private final List<PlayerDropItem> playerDrop = new FastList<>();
    private boolean newPlayer = false;
    private long creationDay;
    private int pvplockills;
    private boolean gp = false;
    private int countGuide = 0;
    private boolean water = false;
    private int saveWorld;
    private int saveInstance;
    private float saveX;
    private float saveY;
    private float saveZ;
    private final List<Integer> accueseAccountId = new FastList<>();
    private boolean accuese = false;
    private boolean banAttack = false;
    private boolean specialShopList = false;
    private boolean saveShop = false;
    private boolean fishing = false;
    private boolean banitems = false;
    private boolean showPacket = false;
    private int prisonLocation = -1;
    private long privateShopTimer;
    private long startPrivateShop;
    private long startGameShop;
    private long gameShopTimer;
    private long teleportTimer;
    private long startTeleport;
    private long startMove;
    private long moveTimer;
    private ChatType chatType;
    public int stigmaCounter;
    private boolean commandUsed;
    private boolean teleport;
    private byte aLevel = 0;
    private byte mLevel = 0;
    private String nicker = "";
    private String nameSearch;
    private String newname = null;
    private String nameFormat = "%s";
    private boolean gm = false;
    private boolean enterWorld;
    private boolean logged;
    private long onlineBonusTime = 0;
    private byte catShop = -1;
    private byte catSubShop = 3;
    private RequestList loadSender;
    private AddItem addItem;
    private Player attacker;
    private Wedding wedding;
    private boolean death = false;
    private List<ItemLog> listItemLog = new ArrayList<>();
    public int sealcount;
    private Copy copy;
    private boolean ffa = false;
    private int targetId;
    private Player player;
    private boolean showBlockList;

    /**
     * Used for JUnit tests
     */
    private Player(PlayerCommonData plCommonData) {
        super(plCommonData.getPlayerObjId(), new PlayerController(), null, plCommonData, null);
        this.playerCommonData = plCommonData;
        this.playerAccount = new Account(0);
        this.absStatsHolder = new AbsoluteStatOwner(this, 0);
    }

    public Player(PlayerController controller, PlayerCommonData plCommonData, PlayerAppearance appereance, Account account) {
        super(plCommonData.getPlayerObjId(), controller, null, plCommonData, plCommonData.getPosition());
        this.daoVars = DAOManager.getDAO(PlayerVarsDAO.class);
        this.playerCommonData = plCommonData;
        this.playerAppearance = appereance;
        this.playerAccount = account;

        this.requester = new ResponseRequester(this);
        this.questStateList = new QuestStateList();
        this.titleList = new TitleList();
        this.portalCooldownList = new PortalCooldownList(this);
        this.craftCooldownList = new CraftCooldownList(this);
        this.houseObjectCooldownList = new HouseObjectCooldownList(this);
        this.toyPetList = new PetList(this);
        controller.setOwner(this);
        moveController = new PlayerMoveController(this);
        plCommonData.setBoundingRadius(new BoundRadius(0.5f, 0.5f, getPlayerAppearance().getHeight()));

        setPlayerStatsTemplate(DataManager.PLAYER_STATS_DATA.getTemplate(this));
        setGameStats(new PlayerGameStats(this));
        setLifeStats(new PlayerLifeStats(this));
        inGameShop = new InGameShop();
        skList = new SerialKiller(this);
        absStatsHolder = new AbsoluteStatOwner(this, 0);
        bonusTime = new PlayerBonusTime();
    }

    public boolean isInPlayerMode(PlayerMode mode) {
        return PlayerActions.isInPlayerMode(this, mode);
    }

    public void setPlayerMode(PlayerMode mode, Object obj) {
        PlayerActions.setPlayerMode(this, mode, obj);
    }

    public void unsetPlayerMode(PlayerMode mode) {
        PlayerActions.unsetPlayerMode(this, mode);
    }

    @Override
    public PlayerMoveController getMoveController() {
        return (PlayerMoveController) super.getMoveController();
    }

    @Override
    protected final AggroList createAggroList() {
        return new PlayerAggroList(this);
    }

    public PlayerCommonData getCommonData() {
        return playerCommonData;
    }

    @Override
    public String getName() {
        return playerCommonData.getName();
    }

    public PlayerAppearance getPlayerAppearance() {
        return playerAppearance;
    }

    public void setPlayerAppearance(PlayerAppearance playerAppearance) {
        this.playerAppearance = playerAppearance;
    }

    /**
     * Only use for the Size admin command
     *
     * @return PlayerAppearance : The saved player's appearance, to rollback his
     * appearance
     */
    public PlayerAppearance getSavedPlayerAppearance() {
        return savedPlayerAppearance;
    }

    /**
     * Only use for the Size admin command
     *
     * @param savedPlayerAppearance PlayerAppearance : The saved player's
     * appearance, to rollback his appearance
     */
    public void setSavedPlayerAppearance(PlayerAppearance savedPlayerAppearance) {
        this.savedPlayerAppearance = savedPlayerAppearance;
    }

    /**
     * Set connection of this player.
     *
     * @param clientConnection
     */
    public void setClientConnection(AionConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    /**
     * Get connection of this player.
     *
     * @return AionConnection of this player.
     */
    public AionConnection getClientConnection() {
        return this.clientConnection;
    }

    public MacroList getMacroList() {
        return macroList;
    }

    public void setMacroList(MacroList macroList) {
        this.macroList = macroList;
    }

    public PlayerSkillList getSkillList() {
        return skillList;
    }

    public void setSkillList(PlayerSkillList skillList) {
        this.skillList = skillList;
    }

    /**
     * @return the toyPet
     */
    public Pet getPet() {
        return toyPet;
    }

    /**
     * @param toyPet the toyPet to set
     */
    public void setToyPet(Pet toyPet) {
        this.toyPet = toyPet;
    }

    /**
     * Gets this players Friend List
     *
     * @return FriendList
     */
    public FriendList getFriendList() {
        return friendList;
    }

    /**
     * Gets this players Request Friend List
     *
     * @return RequestFriendList
     */
    public RequestFriendList getRequestFriendList() {
        return requestFriendList;
    }

    /**
     * Is this player looking for a group
     *
     * @return true or false
     */
    public boolean isLookingForGroup() {
        return lookingForGroup;
    }

    /**
     * Sets whether or not this player is looking for a group
     *
     * @param lookingForGroup
     */
    public void setLookingForGroup(boolean lookingForGroup) {
        this.lookingForGroup = lookingForGroup;
    }

    public boolean isAttackMode() {
        return isAttackMode;
    }

    public void setAttackMode(boolean isAttackMode) {
        this.isAttackMode = isAttackMode;
    }

    public boolean isNotGatherable() {
        return gatherableTimer != 0;
    }

    public void setGatherableTimer(long gatherableTimer) {
        if (gatherableTimer < 0) {
            gatherableTimer = 0;
        }

        this.gatherableTimer = gatherableTimer;
    }

    public long getGatherableTimer() {
        return gatherableTimer;
    }

    public long getStopGatherable() {
        return stopGatherable;
    }

    public void setStopGatherable(long stopGatherable) {
        this.stopGatherable = stopGatherable;
    }

    public String getCaptchaWord() {
        return captchaWord;
    }

    public void setCaptchaWord(String captchaWord) {
        this.captchaWord = captchaWord;
    }

    public byte[] getCaptchaImage() {
        return captchaImage;
    }

    public void setCaptchaImage(byte[] captchaImage) {
        this.captchaImage = captchaImage;
    }

    /**
     * Sets this players friend list. <br />
     * Remember to send the player the <tt>SM_FRIEND_LIST</tt> packet.
     *
     * @param list
     */
    public void setFriendList(FriendList list) {
        this.friendList = list;
    }

    public void setRequestFriendList(RequestFriendList list) {
        this.requestFriendList = list;
    }

    public boolean isShowBlockList() {
        return showBlockList;
    }

    public void setShowBlockList(boolean show) {
        this.showBlockList = show;
    }

    public BlockList getBlockList() {
        return blockList;
    }

    public void setBlockList(BlockList list) {
        this.blockList = list;
    }

    public final PetList getPetList() {
        return toyPetList;
    }

    @Override
    public PlayerLifeStats getLifeStats() {
        return (PlayerLifeStats) super.getLifeStats();
    }

    @Override
    public PlayerGameStats getGameStats() {
        return (PlayerGameStats) super.getGameStats();
    }

    /**
     * Gets the ResponseRequester for this player
     *
     * @return ResponseRequester
     */
    public ResponseRequester getResponseRequester() {
        return requester;
    }

    public boolean isOnline() {
        return getClientConnection() != null;
    }

    public int getQuestExpands() {
        return this.playerCommonData.getQuestExpands();
    }

    public int getNpcExpands() {
        return this.playerCommonData.getNpcExpands();
    }

    public void setCubeLimit() {
        getInventory().setLimit(StorageType.CUBE.getLimit() + (getNpcExpands() + getQuestExpands()) * getInventory().getRowLength());
    }

    public PlayerClass getPlayerClass() {
        return playerCommonData.getPlayerClass();
    }

    public Gender getGender() {
        return playerCommonData.getGender();
    }

    /**
     * Return PlayerController of this Player Object.
     *
     * @return PlayerController.
     */
    @Override
    public PlayerController getController() {
        return (PlayerController) super.getController();
    }

    @Override
    public byte getLevel() {
        return (byte) playerCommonData.getLevel();
    }

    /**
     * @return the inventory
     */
    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * @return the player private store
     */
    public PrivateStore getStore() {
        return store;
    }

    /**
     * @param store the store that needs to be set
     */
    public void setStore(PrivateStore store) {
        this.store = store;
    }

    /**
     * @return the questStatesList
     */
    public QuestStateList getQuestStateList() {
        return questStateList;
    }

    /**
     * @param questStateList the QuestStateList to set
     */
    public void setQuestStateList(QuestStateList questStateList) {
        this.questStateList = questStateList;
    }

    /**
     * @return the playerStatsTemplate
     */
    public PlayerStatsTemplate getPlayerStatsTemplate() {
        return playerStatsTemplate;
    }

    /**
     * @param playerStatsTemplate the playerStatsTemplate to set
     */
    public void setPlayerStatsTemplate(PlayerStatsTemplate playerStatsTemplate) {
        this.playerStatsTemplate = playerStatsTemplate;
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(RecipeList recipeList) {
        this.recipeList = recipeList;
    }

    /**
     * @param storage
     * @param storageType
     */
    public void setStorage(Storage storage, StorageType storageType) {
        if (storageType == StorageType.CUBE) {
            this.inventory = storage;
        }
        if (storageType.getId() >= StorageType.PET_BAG_MIN && storageType.getId() <= StorageType.PET_BAG_MAX) {
            this.petBag[storageType.getId() - StorageType.PET_BAG_MIN] = storage;
        }
        if (storageType.getId() >= StorageType.HOUSE_WH_MIN && storageType.getId() <= StorageType.HOUSE_WH_MAX) {
            this.cabinets[storageType.getId() - StorageType.HOUSE_WH_MIN] = storage;
        }
        if (storageType == StorageType.REGULAR_WAREHOUSE) {
            this.regularWarehouse = storage;
        }
        if (storageType == StorageType.ACCOUNT_WAREHOUSE) {
            this.accountWarehouse = storage;
        }
        storage.setOwner(this);
    }

    /**
     * @param storageType
     * @return
     */
    public IStorage getStorage(int storageType) {
        if (storageType == StorageType.REGULAR_WAREHOUSE.getId()) {
            return regularWarehouse;
        }

        if (storageType == StorageType.ACCOUNT_WAREHOUSE.getId()) {
            return accountWarehouse;
        }

        if (storageType == StorageType.LEGION_WAREHOUSE.getId() && getLegion() != null) {
            return new LegionStorageProxy(getLegion().getLegionWarehouse(), this);
        }

        if (storageType >= StorageType.PET_BAG_MIN && storageType <= StorageType.PET_BAG_MAX) {
            return petBag[storageType - StorageType.PET_BAG_MIN];
        }

        if (storageType >= StorageType.HOUSE_WH_MIN && storageType <= StorageType.HOUSE_WH_MAX) {
            return cabinets[storageType - StorageType.HOUSE_WH_MIN];
        }

        if (storageType == StorageType.CUBE.getId()) {
            return inventory;
        }
        return null;
    }

    /**
     * Items from UPDATE_REQUIRED storages and equipment
     *
     * @return
     */
    public List<Item> getDirtyItemsToUpdate() {
        List<Item> dirtyItems = new ArrayList<>();

        IStorage cubeStorage = getStorage(StorageType.CUBE.getId());
        if (cubeStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
            dirtyItems.addAll(cubeStorage.getItemsWithKinah());
            dirtyItems.addAll(cubeStorage.getDeletedItems());
            cubeStorage.setPersistentState(PersistentState.UPDATED);
        }

        IStorage regularWhStorage = getStorage(StorageType.REGULAR_WAREHOUSE.getId());
        if (regularWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
            dirtyItems.addAll(regularWhStorage.getItemsWithKinah());
            dirtyItems.addAll(regularWhStorage.getDeletedItems());
            regularWhStorage.setPersistentState(PersistentState.UPDATED);
        }

        IStorage accountWhStorage = getStorage(StorageType.ACCOUNT_WAREHOUSE.getId());
        if (accountWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
            dirtyItems.addAll(accountWhStorage.getItemsWithKinah());
            dirtyItems.addAll(accountWhStorage.getDeletedItems());
            accountWhStorage.setPersistentState(PersistentState.UPDATED);
        }

        IStorage legionWhStorage = getStorage(StorageType.LEGION_WAREHOUSE.getId());
        if (legionWhStorage != null) {
            if (legionWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
                dirtyItems.addAll(legionWhStorage.getItemsWithKinah());
                dirtyItems.addAll(legionWhStorage.getDeletedItems());
                legionWhStorage.setPersistentState(PersistentState.UPDATED);
            }
        }

        for (int petBagId = StorageType.PET_BAG_MIN; petBagId <= StorageType.PET_BAG_MAX; petBagId++) {
            IStorage petBag2 = getStorage(petBagId);
            if (petBag2 != null && petBag2.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
                dirtyItems.addAll(petBag2.getItemsWithKinah());
                dirtyItems.addAll(petBag2.getDeletedItems());
                petBag2.setPersistentState(PersistentState.UPDATED);
            }
        }

        for (int houseWhId = StorageType.HOUSE_WH_MIN; houseWhId <= StorageType.HOUSE_WH_MAX; houseWhId++) {
            IStorage cabinet = getStorage(houseWhId);
            if (cabinet != null && cabinet.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
                dirtyItems.addAll(cabinet.getItemsWithKinah());
                dirtyItems.addAll(cabinet.getDeletedItems());
                cabinet.setPersistentState(PersistentState.UPDATED);
            }
        }

        Equipment equipment2 = getEquipment();
        if (equipment2.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
            dirtyItems.addAll(equipment2.getEquippedItems());
            equipment2.setPersistentState(PersistentState.UPDATED);
        }

        return dirtyItems;
    }

    /**
     * //TODO probably need to optimize here
     *
     * @return
     */
    public FastList<Item> getAllItems() {
        FastList<Item> items = FastList.newInstance();
        items.addAll(this.inventory.getItemsWithKinah());
        if (this.regularWarehouse != null) {
            items.addAll(this.regularWarehouse.getItemsWithKinah());
        }
        if (this.accountWarehouse != null) {
            items.addAll(this.accountWarehouse.getItemsWithKinah());
        }

        for (int petBagId = StorageType.PET_BAG_MIN; petBagId <= StorageType.PET_BAG_MAX; petBagId++) {
            IStorage petBags = getStorage(petBagId);
            if (petBags != null) {
                items.addAll(petBags.getItemsWithKinah());
            }
        }

        for (int houseWhId = StorageType.HOUSE_WH_MIN; houseWhId <= StorageType.HOUSE_WH_MAX; houseWhId++) {
            IStorage cabinet = getStorage(houseWhId);
            if (cabinet != null) {
                items.addAll(cabinet.getItemsWithKinah());
            }
        }

        items.addAll(getEquipment().getEquippedItems());
        return items;
    }

    public Storage getInventory() {
        return inventory;
    }

    /**
     * @return the playerSettings
     */
    public PlayerSettings getPlayerSettings() {
        return playerSettings;
    }

    /**
     * @param playerSettings the playerSettings to set
     */
    public void setPlayerSettings(PlayerSettings playerSettings) {
        this.playerSettings = playerSettings;
    }

    public TitleList getTitleList() {
        return titleList;
    }

    public void setTitleList(TitleList titleList) {
        if (havePermission(MembershipConfig.TITLES_ADDITIONAL_ENABLE)) {
            for (int i : new int[]{102, 103, 104, 105, 106, 146, 151, 152, 160, 161}) {
                titleList.addEntry(i, 0);
            }
        }
        this.titleList = titleList;
        titleList.setOwner(this);
    }

    public com.aionemu.gameserver.model.team2.group.PlayerGroup getPlayerGroup2() {
        return playerGroup2;
    }

    public void setPlayerGroup2(com.aionemu.gameserver.model.team2.group.PlayerGroup playerGroup) {
        this.playerGroup2 = playerGroup;
    }

    /**
     * @return the abyssRank
     */
    public AbyssRank getAbyssRank() {
        return abyssRank;
    }

    /**
     * @param abyssRank the abyssRank to set
     */
    public void setAbyssRank(AbyssRank abyssRank) {
        this.abyssRank = abyssRank;
    }

    @Override
    public PlayerEffectController getEffectController() {
        return (PlayerEffectController) super.getEffectController();
    }

    public void onLoggedIn() {
        friendList.setStatus(Status.ONLINE, getCommonData());
        loadSender.setStatus(RequestList.Status.ONLINE, getCommonData());
    }

    public void onLoggedOut() {
        requester.denyAll();
        friendList.setStatus(FriendList.Status.OFFLINE, getCommonData());
        loadSender.setStatus(RequestList.Status.OFFLINE, getCommonData());
    }

    /**
     * Returns true if has valid LegionMember
     *
     * @return
     */
    public boolean isLegionMember() {
        return legionMember != null;
    }

    /**
     * @param legionMember the legionMember to set
     */
    public void setLegionMember(LegionMember legionMember) {
        this.legionMember = legionMember;
    }

    /**
     * @return the legionMember
     */
    public LegionMember getLegionMember() {
        return legionMember;
    }

    /**
     * @return the legion
     */
    public Legion getLegion() {
        return legionMember != null ? legionMember.getLegion() : null;
    }

    /**
     * Checks if object id's are the same
     *
     * @param objectId
     * @return true if the object id is the same
     */
    public boolean sameObjectId(int objectId) {
        return this.getObjectId() == objectId;
    }

    /**
     * @return true if a player has a store opened
     */
    public boolean hasStore() {
        return getStore() != null;
    }

    /**
     * Removes legion from player
     */
    public void resetLegionMember() {
        setLegionMember(null);
    }

    public boolean isInGroup2() {
        return playerGroup2 != null;
    }

    /**
     * Access level of this player
     *
     * @return byte
     */
    public byte getAccessLevel() {
        return aLevel;
    }

    public void initializeLevels() {
        byte level = (byte) (playerAccount.isGameMaster() ? getSecurityLevel() : playerAccount.getAccessLevel());
        aLevel = level;
        if (aLevel >= AdminConfig.GM_LEVEL) {
            this.gm = true;
        }
        byte membership = (byte) (playerAccount.isPlayerMember() ? getMemberLevel() : playerAccount.getMembership());
        mLevel = membership;
        if (getCommonData().getTimeAccessLevelExpire() != null && System.currentTimeMillis() > getCommonData().getTimeAccessLevelExpire().getTime()) {
            setSecurityLevel(0);
        }
        if (getCommonData().getTimeMembershipLevelExpire() != null && System.currentTimeMillis() > getCommonData().getTimeMembershipLevelExpire().getTime()) {
            setMembershipLevel(0);
        }
        if (isDeveloper()) {
            death = true;
        }
    }

    //todo
    public void reloadLevels() {
        byte level = (byte) (playerAccount.isGameMaster() ? getSecurityLevel() : playerAccount.getAccessLevel());
        aLevel = level;
        if (aLevel >= AdminConfig.GM_LEVEL) {
            this.gm = true;
        }
        byte membership = (byte) (playerAccount.isPlayerMember() ? getMemberLevel() : playerAccount.getMembership());
        mLevel = membership;
        if (getCommonData().getTimeAccessLevelExpire() != null && System.currentTimeMillis() > getCommonData().getTimeAccessLevelExpire().getTime()) {
            setSecurityLevel(0);
        }
        if (getCommonData().getTimeMembershipLevelExpire() != null && System.currentTimeMillis() > getCommonData().getTimeMembershipLevelExpire().getTime()) {
            setMembershipLevel(0);
        }
    }

    public int getSecurityLevel() {
        return getCommonData().getSecurityLevel();
    }

    public void setSecurityLevel(int i) {
        getCommonData().setSecurityLevel(i);
    }

    /**
     * accountName of this player
     *
     * @return int
     */
    public String getAcountName() {
        return playerAccount.getName();
    }

    /**
     * @return the rates
     */
    public Rates getRates() {
        if (rates == null) {
            rates = new RegularRates();
        }
        return rates;
    }

    /**
     * @param rates the rates to set
     */
    public void setRates(Rates rates) {
        this.rates = rates;
    }

    /**
     * @return warehouse size
     */
    public int getWarehouseSize() {
        return this.playerCommonData.getWhNpcExpands() + this.playerCommonData.getWhBonusExpands();
    }

    public int getWhNpcExpands() {
        return this.playerCommonData.getWhNpcExpands();
    }

    public int getWhBonusExpands() {
        return this.playerCommonData.getWhBonusExpands();
    }

    public void setWarehouseLimit() {
        getWarehouse().setLimit(StorageType.REGULAR_WAREHOUSE.getLimit() + (getWarehouseSize() * getWarehouse().getRowLength()));
    }

    /**
     * @return regularWarehouse
     */
    public Storage getWarehouse() {
        return regularWarehouse;
    }

    /**
     * 0: regular, 1: fly, 2: glide
     *
     * @return
     */
    public int getFlyState() {
        return this.flyState;
    }

    public void setFlyState(int flyState) {
        this.flyState = flyState;
        if (flyState == 1) {
            setFlyingMode(true);
        } else if (flyState == 0) {
            setFlyingMode(false);
        }
    }

    /**
     * @return the isTrading
     */
    public boolean isTrading() {
        return isTrading;
    }

    /**
     * @param isTrading the isTrading to set
     */
    public void setTrading(boolean isTrading) {
        this.isTrading = isTrading;
    }

    /**
     * @return the isInPrison
     */
    public boolean isInPrison() {
        return prisonTimer != 0;
    }

    /**
     * @param prisonTimer the prisonTimer to set
     */
    public void setPrisonTimer(long prisonTimer) {
        if (prisonTimer < 0) {
            prisonTimer = 0;
        }

        this.prisonTimer = prisonTimer;
    }

    /**
     * @return the prisonTimer
     */
    public long getPrisonTimer() {
        return prisonTimer;
    }

    /**
     * @return the time in ms of start prison
     */
    public long getStartPrison() {
        return startPrison;
    }

    /**
     * @param start : The time in ms of start prison
     */
    public void setStartPrison(long start) {
        this.startPrison = start;
    }

    /**
     * @return
     */
    public boolean isProtectionActive() {
        return isInVisualState(CreatureVisualState.BLINKING);
    }

    /**
     * Check is player is invul
     *
     * @return boolean
     *
     */
    public boolean isInvul() {
        return invul;
    }

    /**
     * Sets invul on player
     *
     * @param invul - boolean
     *
     */
    public void setInvul(boolean invul) {
        this.invul = invul;
    }

    public void setMailbox(Mailbox mailbox) {
        this.mailbox = mailbox;
    }

    public Mailbox getMailbox() {
        return mailbox;
    }

    /**
     * @return the flyController
     */
    public FlyController getFlyController() {
        return flyController;
    }

    /**
     * @param flyController the flyController to set
     */
    public void setFlyController(FlyController flyController) {
        this.flyController = flyController;
    }

    public int getLastOnline() {
        Timestamp lastOnline = playerCommonData.getLastOnline();
        if (lastOnline == null || isOnline()) {
            return 0;
        }

        return (int) (lastOnline.getTime() / 1000);
    }

    /**
     * @param craftingTask
     */
    public void setCraftingTask(CraftingTask craftingTask) {
        this.craftingTask = craftingTask;
    }

    /**
     * @return
     */
    public CraftingTask getCraftingTask() {
        return craftingTask;
    }

    /**
     * @param flightTeleportId
     */
    public void setFlightTeleportId(int flightTeleportId) {
        this.flightTeleportId = flightTeleportId;
    }

    /**
     * @return flightTeleportId
     */
    public int getFlightTeleportId() {
        return flightTeleportId;
    }

    /**
     * @param flightDistance
     */
    public void setFlightDistance(int flightDistance) {
        this.flightDistance = flightDistance;

    }

    /**
     * @param path
     */
    public void setCurrentFlypath(FlyPathEntry path) {
        this.flyLocationId = path;
        if (path != null) {
            this.flyStartTime = System.currentTimeMillis();
        } else {
            this.flyStartTime = 0;
        }
    }

    /**
     * @return flightDistance
     */
    public int getFlightDistance() {
        return flightDistance;
    }

    /**
     * @return
     */
    public boolean isUsingFlyTeleport() {
        return isInState(CreatureState.FLIGHT_TELEPORT) && flightTeleportId != 0;
    }

    public boolean isGM() {
        return gm;
    }

    @Override
    public boolean isEnemy(Creature creature) {
        return creature.isEnemyFrom(this) || this.isEnemyFrom(creature);
    }

    @Override
    public boolean isEnemyFrom(Npc enemy) {
        switch (CreatureType.getCreatureType(enemy.getType(this))) {
            case AGGRESSIVE:
            case ATTACKABLE:
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * Player enemies:<br>
     * - different race<br>
     * - duel partner<br>
     * - in pvp zone
     *
     * @return
     */
    @Override
    public boolean isEnemyFrom(Player player) {
        if (this.equals(player)) {
            return false;
        } else {
            return isAdminEnmity(player) || canPvP(player) || this.getController().isDueling(player);
        }
    }

    public boolean isAggroIconTo(Player player) {
        if (isAdminEnmity(player)) {
            return true;
        }
        return !player.getRace().equals(getRace());
    }

    private boolean isAdminEnmity(Player player) {
        return getAdminEnmity() > 1 || player.getAdminEnmity() > 1
                || getPosition().getWorldMapInstance().getInstanceHandler().isEnemy(this, player)
                || player.isInFfa() || MixFight.isInFFA(player) && player.isInGroup2()
                && this.isInGroup2() && player.getPlayerGroup2() != this.getPlayerGroup2()
                || FfaGroupService.isInFFA(player) && player.isInGroup2() && this.isInGroup2()
                && player.getPlayerGroup2() != this.getPlayerGroup2()
                || FfaLegionService.isInFFA(player) && player.getLegion() != null
                && this.getLegion() != null && player.getLegion() != this.getLegion();
        /*|| this.getLegion().isWar(player.getLegion()))
         || this.getLegion() != null && this.getLegion().isHermit() && this.getLegion() != player.getLegion()
         || player.getLegion() != null && player.getLegion().isHermit() && player.getLegion() != this.getLegion()*/
    }

    private boolean isPvP(Player player) {
        int worldId = player.getWorldId();
        if (!player.getRace().equals(getRace())) {
            if (worldId == 600020000 || worldId == 600030000 || worldId == 710010000) {
                return isInsideZoneType(ZoneType.PVP) && player.isInsideZoneType(ZoneType.PVP);
            }
            return true;
        }
        return false;
    }

    private boolean canPvP(Player enemy) {
        if (getPosition().getWorldMapInstance().getInstanceHandler().isEnemy(this, enemy)
                || getPosition().getWorldMapInstance().isPvPLocation() && enemy.getRace() != getRace()) {
            return true;
        }
        int worldId = enemy.getWorldId();
        if (enemy.getRace() != getRace()) {
            if (worldId == 600020000 || worldId == 600030000) {
                if (DisputeLandService.getInstance().isActive()) {
                    return (!this.isInDisablePvPZone() && !enemy.isInDisablePvPZone());
                } else {
                    return (this.isInsideZoneType(ZoneType.PVP)
                            && enemy.isInsideZoneType(ZoneType.PVP)
                            && !this.isInDisablePvPZone() && !enemy.isInDisablePvPZone());
                }
            } else if (worldId == 210040000 || worldId == 220040000 || worldId == 600050000 || worldId == 600060000 || worldId == 600070000) {
                return (!this.isInDisablePvPZone() && !enemy.isInDisablePvPZone());
            } else {
                return true;
            }
        } else {
            if (worldId != 600020000 && worldId != 600030000) {
                return (this.isInsideZoneType(ZoneType.PVP) && enemy.isInsideZoneType(ZoneType.PVP) && !isInSameTeam(enemy));
            }
        }

        return false;
    }

    public boolean isInDisablePvPZone() {
        List<ZoneInstance> zones = this.getPosition().getMapRegion().getZones(this);
        for (ZoneInstance zone : zones) {
            if (!zone.isPvpAllowed()) {
                return true;
            }
        }

        return false;
    }

    // robit
    @SuppressWarnings("unused")
    public boolean isInsideDisablePvpZone() {
        for (ZoneInstance zone : this.getPosition().getMapRegion().getZones(this)) {
            if (this.isInsideZoneType(ZoneType.PVP) && zone.getZoneTemplate().getFlags() == 0) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public boolean isInPvPZone() {
        List<ZoneInstance> zones = this.getPosition().getMapRegion().getZones(this);
        for (ZoneInstance zone : zones) {
            if (zone.isPvpAllowed()) {
                return true;
            }
        }

        return false;
    }

    public boolean isInSameTeam(Player player) {
        if (isInGroup2() && player.isInGroup2()) {
            return getPlayerGroup2().getTeamId() == player.getPlayerGroup2().getTeamId();
        } else if (isInAlliance2() && player.isInAlliance2()) {
            return getPlayerAlliance2().getObjectId().equals(player.getPlayerAlliance2().getObjectId());
        } else if (isInLeague() && player.isInLeague()) {
            return getPlayerAllianceGroup2().getObjectId().equals(player.getPlayerAllianceGroup2().getObjectId());
        }
        return false;
    }

    @Override
    public boolean canSee(Creature creature) {
        if (creature.isInVisualState(CreatureVisualState.BLINKING)) {
            return true;
        }

        if (creature instanceof Player && isInSameTeam((Player) creature)) {
            return true;
        }

        if (creature instanceof Trap && ((Trap) creature).getCreator().equals(this)) {
            return true;
        }

        if (creature instanceof Kisk && (((Kisk) creature).getCreator().equals(this) || ((Kisk) creature).getCreator().isInSameTeam(this))) {
            return true;
        }

        if (creature instanceof Servant && (((Servant) creature).getCreator().equals(this) || ((Npc) creature).getCreator() instanceof Player && ((Player) ((Npc) creature).getCreator()).isInSameTeam(this))) {
            return true;
        }

        if (creature instanceof Summon && (((Summon) creature).getMaster().equals(this) || ((Summon) creature).getMaster().isInSameTeam(this))) {
            return true;
        }

        return creature.getVisualState() <= getSeeState();
    }

    @Override
    public TribeClass getTribe() {
        TribeClass transformTribe = getTransformModel().getTribe();
        if (transformTribe != null) {
            return transformTribe;
        }
        return getRace() == Race.ELYOS ? TribeClass.PC : TribeClass.PC_DARK;
    }

    @Override
    public TribeClass getBaseTribe() {
        TribeClass transformTribe = getTransformModel().getTribe();
        if (transformTribe != null) {
            return DataManager.TRIBE_RELATIONS_DATA.getBaseTribe(transformTribe);
        }
        return getTribe();
    }

    /**
     * @return the summon
     */
    public Summon getSummon() {
        return summon;
    }

    /**
     * @param summon the summon to set
     */
    public void setSummon(Summon summon) {
        this.summon = summon;
    }

    /**
     * @return the summoned object
     */
    public SummonedObject<?> getSummonedObj() {
        return summonedObj;
    }

    /**
     * @param summonedObj the summoned object to set
     */
    public void setSummonedObj(SummonedObject<?> summonedObj) {
        this.summonedObj = summonedObj;
    }

    /**
     * @param newKisk
     */
    public void setKisk(Kisk newKisk) {
        this.kisk = newKisk;
    }

    /**
     * @return
     */
    public Kisk getKisk() {
        return this.kisk;
    }

    /**
     * @param limits
     * @return
     */
    public boolean isItemUseDisabled(ItemUseLimits limits) {
        if (limits == null) {
            return false;
        }

        if (itemCoolDowns == null || !itemCoolDowns.containsKey(limits.getDelayId())) {
            return false;
        }

        Long coolDown = itemCoolDowns.get(limits.getDelayId()).getReuseTime();
        if (coolDown == null) {
            return false;
        }

        if (coolDown < System.currentTimeMillis()) {
            itemCoolDowns.remove(limits.getDelayId());
            return false;
        }

        return true;
    }

    /**
     * @param delayId
     * @return
     */
    public long getItemCoolDown(int delayId) {
        if (itemCoolDowns == null || !itemCoolDowns.containsKey(delayId)) {
            return 0;
        }

        return itemCoolDowns.get(delayId).getReuseTime();
    }

    /**
     * @return the itemCoolDowns
     */
    public Map<Integer, ItemCooldown> getItemCoolDowns() {
        return itemCoolDowns;
    }

    /**
     * @param delayId
     * @param time
     * @param useDelay
     */
    public void addItemCoolDown(int delayId, long time, int useDelay) {
        if (itemCoolDowns == null) {
            itemCoolDowns = new FastMap<Integer, ItemCooldown>().shared();
        }

        itemCoolDowns.put(delayId, new ItemCooldown(time, useDelay));
    }

    /**
     * @param itemMask
     */
    public void removeItemCoolDown(int itemMask) {
        if (itemCoolDowns == null) {
            return;
        }
        itemCoolDowns.remove(itemMask);
    }

    /**
     * @return isAdminTeleportation
     */
    public boolean getAdminTeleportation() {
        return isAdminTeleportation;
    }

    /**
     * @param isAdminTeleportation
     */
    public void setAdminTeleportation(boolean isAdminTeleportation) {
        this.isAdminTeleportation = isAdminTeleportation;
    }

    public final boolean isCoolDownZero() {
        return cooldownZero;
    }

    public final void setCoolDownZero(boolean cooldownZero) {
        this.cooldownZero = cooldownZero;
    }

    public void setPlayerResActivate(boolean isActivated) {
        this.isResByPlayer = isActivated;
    }

    public boolean getResStatus() {
        return isResByPlayer;
    }

    public int getResurrectionSkill() {
        return resurrectionSkill;
    }

    public void setResurrectionSkill(int resurrectionSkill) {
        this.resurrectionSkill = resurrectionSkill;
    }

    public void setIsFlyingBeforeDeath(boolean isActivated) {
        this.isFlyingBeforeDeath = isActivated;
    }

    public boolean getIsFlyingBeforeDeath() {
        return isFlyingBeforeDeath;
    }

    public com.aionemu.gameserver.model.team2.alliance.PlayerAlliance getPlayerAlliance2() {
        return playerAllianceGroup != null ? playerAllianceGroup.getAlliance() : null;
    }

    public PlayerAllianceGroup getPlayerAllianceGroup2() {
        return playerAllianceGroup;
    }

    public boolean isInAlliance2() {
        return playerAllianceGroup != null;
    }

    public void setPlayerAllianceGroup2(PlayerAllianceGroup playerAllianceGroup) {
        this.playerAllianceGroup = playerAllianceGroup;
    }

    public final boolean isInLeague() {
        return isInAlliance2() && getPlayerAlliance2().isInLeague();
    }

    public final boolean isInTeam() {
        return isInGroup2() || isInAlliance2();
    }

    /**
     * @return current {@link PlayerGroup}, {@link PlayerAlliance} or null
     */
    public final TemporaryPlayerTeam<? extends TeamMember<Player>> getCurrentTeam() {
        return isInGroup2() ? getPlayerGroup2() : getPlayerAlliance2();
    }

    /**
     * @return current {@link PlayerGroup}, {@link PlayerAllianceGroup} or null
     */
    public final TemporaryPlayerTeam<? extends TeamMember<Player>> getCurrentGroup() {
        return isInGroup2() ? getPlayerGroup2() : getPlayerAllianceGroup2();
    }

    /**
     * @return current team id
     */
    public final int getCurrentTeamId() {
        return isInTeam() ? getCurrentTeam().getTeamId() : 0;
    }

    /**
     * @return
     */
    public PortalCooldownList getPortalCooldownList() {
        return portalCooldownList;
    }

    public CraftCooldownList getCraftCooldownList() {
        return craftCooldownList;
    }

    public HouseObjectCooldownList getHouseObjectCooldownList() {
        return houseObjectCooldownList;
    }

    public SerialKiller getSKInfo() {
        return skList;
    }

    public void setSKInfo(SerialKiller serialKiller) {
        skList = serialKiller;
    }

    /**
     * @author IlBuono
     * @param edit_mode
     */
    public void setEditMode(boolean edit_mode) {
        this.edit_mode = edit_mode;
    }

    /**
     * @author IlBuono
     * @return
     */
    public boolean isInEditMode() {
        return edit_mode;
    }

    public Npc getPostman() {
        return postman;
    }

    public void setPostman(Npc postman) {
        this.postman = postman;
    }

    public Account getPlayerAccount() {
        return playerAccount;
    }

    /**
     * Quest completion
     *
     * @param questId
     * @return
     */
    public boolean isCompleteQuest(int questId) {
        QuestState qs = getQuestStateList().getQuestState(questId);

        if (qs == null) {
            return false;
        }

        return qs.getStatus() == QuestStatus.COMPLETE;
    }

    public long getNextSkillUse() {
        return nextSkillUse;
    }

    public void setNextSkillUse(long nextSkillUse) {
        this.nextSkillUse = nextSkillUse;
    }

    public long getNextSummonSkillUse() {
        return nextSummonSkillUse;
    }

    public void setNextSummonSkillUse(long nextSummonSkillUse) {
        this.nextSummonSkillUse = nextSummonSkillUse;
    }

    /**
     * chain skills
     *
     * @return
     */
    public ChainSkills getChainSkills() {
        if (this.chainSkills == null) {
            this.chainSkills = new ChainSkills();
        }
        return this.chainSkills;
    }

    public void setLastCounterSkill(AttackStatus status) {
        long time = System.currentTimeMillis();
        if (AttackStatus.getBaseStatus(status) == AttackStatus.DODGE && PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.SCOUT) {
            this.lastCounterSkill.put(AttackStatus.DODGE, time);
        } else if (AttackStatus.getBaseStatus(status) == AttackStatus.PARRY
                && (getPlayerClass() == PlayerClass.GLADIATOR || getPlayerClass() == PlayerClass.CHANTER)) {
            this.lastCounterSkill.put(AttackStatus.PARRY, time);
        } else if (AttackStatus.getBaseStatus(status) == AttackStatus.BLOCK
                && PlayerClass.getStartingClassFor(getPlayerClass()) == PlayerClass.WARRIOR) {
            this.lastCounterSkill.put(AttackStatus.BLOCK, time);
        }
    }

    public long getLastCounterSkill(AttackStatus status) {
        if (this.lastCounterSkill.get(status) == null) {
            return 0;
        }

        return this.lastCounterSkill.get(status);
    }

    /**
     * @return the dualEffectValue
     */
    public int getDualEffectValue() {
        return dualEffectValue;
    }

    /**
     * @param dualEffectValue the dualEffectValue to set
     */
    public void setDualEffectValue(int dualEffectValue) {
        this.dualEffectValue = dualEffectValue;
    }

    /**
     * @return the Resurrection Positional State
     */
    public boolean isInResPostState() {
        return this.isInResurrectPosState;
    }

    /**
     * @param value
     */
    public void setResPosState(boolean value) {
        this.isInResurrectPosState = value;
    }

    /**
     * @param value
     */
    public void setResPosX(float value) {
        this.resPosX = value;
    }

    /**
     * @return the Resurrection Positional X value
     */
    public float getResPosX() {
        return this.resPosX;
    }

    /**
     * @param value the Resurrection Positional Y value to set
     */
    public void setResPosY(float value) {
        this.resPosY = value;
    }

    /**
     * @return the Resurrection Positional Y value
     */
    public float getResPosY() {
        return this.resPosY;
    }

    /**
     * @param value the Resurrection Positional Z value to set
     */
    public void setResPosZ(float value) {
        this.resPosZ = value;
    }

    /**
     * @return the Resurrection Positional Z value
     */
    public float getResPosZ() {
        return this.resPosZ;
    }

    public boolean isInSiegeWorld() {
        switch (getWorldId()) {
            case 210050000:
            case 220070000:
            case 400010000:
            case 600030000:
            case 600050000:
            case 600060000:
                return true;
            default:
                return false;
        }
    }

    /**
     * @return true if player is under NoFly Effect
     */
    public boolean isUnderNoFly() {
        return this.getEffectController().isAbnormalSet(AbnormalState.NOFLY);
    }

    /**
     * @param value status of NoFpConsum Effect
     */
    public void setUnderNoFPConsum(boolean value) {
        this.underNoFPConsum = value;
    }

    /**
     * @return true if player is under NoFpConsumEffect
     */
    public boolean isUnderNoFPConsum() {
        return this.underNoFPConsum;
    }

    public void setInstanceStartPos(float instanceStartPosX, float instanceStartPosY, float instanceStartPosZ) {
        this.instanceStartPosX = instanceStartPosX;
        this.instanceStartPosY = instanceStartPosY;
        this.instanceStartPosZ = instanceStartPosZ;
    }

    public float getInstanceStartPosX() {
        return instanceStartPosX;
    }

    public float getInstanceStartPosY() {
        return instanceStartPosY;
    }

    public float getInstanceStartPosZ() {
        return instanceStartPosZ;
    }

    public void setMembershipLevel(int aInt) {
        getCommonData().setMembershipLevel(aInt);
    }

    public int getMemberLevel() {
        return getCommonData().getMemberLevel();
    }

    public boolean havePermission(byte perm) {
        return getMembership() >= perm;
    }

    /**
     * @return Returns the emotions.
     */
    public EmotionList getEmotions() {
        return emotions;
    }

    /**
     * @param emotions The emotions to set.
     */
    public void setEmotions(EmotionList emotions) {
        this.emotions = emotions;
    }

    public int getRebirthResurrectPercent() {
        return rebirthResurrectPercent;
    }

    public void setRebirthResurrectPercent(int rebirthResurrectPercent) {
        this.rebirthResurrectPercent = rebirthResurrectPercent;
    }

    public int getRebirthSkill() {
        return rebirthSkill;
    }

    public void setRebirthSkill(int rebirthSkill) {
        this.rebirthSkill = rebirthSkill;
    }

    public BindPointPosition getBindPoint() {
        return bindPoint;
    }

    public void setBindPoint(BindPointPosition bindPoint) {
        this.bindPoint = bindPoint;
    }

    public int speedHackCounter;
    public int abnormalHackCounter;
    public long prevPosUT;
    public byte prevMoveType;
    private WorldPosition prevPos;

    @Override
    public ItemAttackType getAttackType() {
        Item weapon = getEquipment().getMainHandWeapon();
        if (weapon != null) {
            return weapon.getItemTemplate().getAttackType();
        }
        return ItemAttackType.PHYSICAL;
    }

    public long getFlyStartTime() {
        return this.flyStartTime;
    }

    public FlyPathEntry getCurrentFlyPath() {
        return flyLocationId;
    }

    public void setUnWispable() {
        this.isWispable = false;
    }

    public void setWispable() {
        this.isWispable = true;
    }

    public boolean isWispable() {
        return isWispable;
    }

    public boolean isInvulnerableWing() {
        return this.isUnderInvulnerableWing;
    }

    public void setInvulnerableWing(boolean value) {
        this.isUnderInvulnerableWing = value;
    }

    public void resetAbyssRankListUpdated() {
        this.abyssRankListUpdateMask = 0;
    }

    public void setAbyssRankListUpdated(AbyssRankUpdateType type) {
        this.abyssRankListUpdateMask |= type.value();
    }

    public boolean isAbyssRankListUpdated(AbyssRankUpdateType type) {
        return (this.abyssRankListUpdateMask & type.value()) == type.value();
    }

    public void addSalvationPoints(long points) {
        this.playerCommonData.addSalvationPoints(points);
        PacketSendUtility.sendPacket(this, new SM_STATS_INFO(this));
    }

    @Override
    public byte isPlayer() {
        if (this.isGM()) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * @return the motions
     */
    public MotionList getMotions() {
        return motions;
    }

    /**
     * @param motions the motions to set
     */
    public void setMotions(MotionList motions) {
        this.motions = motions;
    }

    public void setTransformed(boolean value) {
        this.getTransformModel().setActive(value);
    }

    public boolean isTransformed() {
        return this.getTransformModel().isActive();
    }

    /**
     * @return the npcFactions
     */
    public NpcFactions getNpcFactions() {
        return npcFactions;
    }

    /**
     * @param npcFactions the npcFactions to set
     */
    public void setNpcFactions(NpcFactions npcFactions) {
        this.npcFactions = npcFactions;
    }

    /**
     * @return the flyReuseTime
     */
    public long getFlyReuseTime() {
        return flyReuseTime;
    }

    /**
     * @param flyReuseTime the flyReuseTime to set
     */
    public void setFlyReuseTime(long flyReuseTime) {
        this.flyReuseTime = flyReuseTime;
    }

    /**
     * @param value flying mode flag to set
     */
    public void setFlyingMode(boolean value) {
        this.isFlying = value;
    }

    /**
     * @return true if player is in Flying mode
     */
    public boolean isInFlyingMode() {
        return this.isFlying;
    }

    /**
     * Stone Use Order determined by highest inventory slot. :( If player has
     * two types, wrong one might be used.
     *
     * @return selfRezItem
     */
    public Item getSelfRezStone() {
        Item item = getReviveStone(161001001);
        if (item == null) {
            item = getReviveStone(161000003);
        }
        if (item == null) {
            item = getReviveStone(161000004);
        }
        if (item == null) {
            item = getReviveStone(161000001);
        }
        return item;
    }

    /**
     * @param stoneItemId
     * @return stoneItem or null
     */
    private Item getReviveStone(int stoneId) {
        Item item = getInventory().getFirstItemByItemId(stoneId);
        if (item != null && isItemUseDisabled(item.getItemTemplate().getUseLimits())) {
            item = null;
        }
        return item;
    }

    /**
     * Need to find how an item is determined as able to self-rez.
     *
     * @return boolean can self rez with item
     */
    public boolean haveSelfRezItem() {
        return (getSelfRezStone() != null);
    }

    /**
     * Rebirth Effect is id 160.
     *
     * @return
     */
    public boolean haveSelfRezEffect() {
        if (getAccessLevel() >= AdminConfig.ADMIN_AUTO_RES) {
            return true;
        }

        // Store the effect info.
        List<Effect> effects = getEffectController().getAbnormalEffects();
        for (Effect effect : effects) {
            for (EffectTemplate template : effect.getEffectTemplates()) {
                if (template.getEffectid() == 160 && template instanceof RebirthEffect) {
                    RebirthEffect rebirthEffect = (RebirthEffect) template;
                    setRebirthResurrectPercent(rebirthEffect.getResurrectPercent());
                    setRebirthSkill(rebirthEffect.getSkillId());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasResurrectBase() {
        List<Effect> effects = getEffectController().getAbnormalEffects();
        for (Effect effect : effects) {
            for (EffectTemplate template : effect.getEffectTemplates()) {
                if (template.getEffectid() == 160 && template instanceof ResurrectBaseEffect) {
                    return true;
                }
            }
        }
        return false;
    }

    public void unsetResPosState() {
        if (isInResPostState()) {
            setResPosState(false);
            setResPosX(0);
            setResPosY(0);
            setResPosZ(0);
        }
    }

    public LootGroupRules getLootGroupRules() {
        if (isInGroup2()) {
            return getPlayerGroup2().getLootGroupRules();
        }
        if (isInAlliance2()) {
            return getPlayerAlliance2().getLootGroupRules();
        }
        return null;
    }

    public boolean isLooting() {
        return lootingNpcOid != 0;
    }

    public void setLootingNpcOid(int lootingNpcOid) {
        this.lootingNpcOid = lootingNpcOid;
    }

    public int getLootingNpcOid() {
        return lootingNpcOid;
    }

    public final boolean isMentor() {
        return isMentor;
    }

    public void setRemoveSkillDelay(int removeSkillDelay) {
        this.removeSkillDelay = removeSkillDelay;
    }

    public int getRemoveSkillDelay() {
        return removeSkillDelay;
    }

    public final void setMentor(boolean isMentor) {
        this.isMentor = isMentor;
    }

    @Override
    public Race getRace() {
        return playerCommonData.getRace();
    }

    private PlayerVarsDAO daoVars;
    private Map<String, Object> vars = FastMap.newInstance();

    public boolean hasVar(String key) {
        return vars.containsKey(key);
    }

    public void setVar(String key, Object value, boolean sql) {
        vars.put(key, value);
        if (sql) {
            daoVars.set(this.getObjectId(), key, value);
        }
    }

    public Object getVar(String key) {
        return this.vars.get(key);
    }

    public int getVarInt(String key) {
        Object o = this.vars.get(key);
        if (o != null) {
            return Integer.parseInt(o.toString());
        }
        return 0;
    }

    public String getVarStr(String key) {
        Object o = this.vars.get(key);
        if (o != null) {
            return o.toString();
        }
        return null;
    }

    public void setVars(Map<String, Object> map) {
        this.vars = map;
    }

    @Override
    public int getSkillCooldown(SkillTemplate template) {
        return isCoolDownZero() ? 0 : template.getCooldown();
    }

    @Override
    public int getItemCooldown(ItemTemplate template) {
        return isCoolDownZero() ? 0 : template.getUseLimits().getDelayTime();
    }

    public void setLastMessageTime() {
        if ((System.currentTimeMillis() - lastMsgTime) / 1000 < SecurityConfig.FLOOD_DELAY) {
            floodMsgCount++;
        } else {
            floodMsgCount = 0;
        }
        lastMsgTime = System.currentTimeMillis();
    }

    public int floodMsgCount() {
        return floodMsgCount;
    }

    public void setOnlineTime() {
        onlineTime = System.currentTimeMillis();
    }

    /*
     * return online time in sec
     */
    public long getOnlineTime() {
        return (System.currentTimeMillis() - onlineTime) / 1000;
    }

    public long getOnlineTime2() {
        return onlineTime;
    }

    public long getOnlineBonusTime() {
        return onlineBonusTime;
    }

    public void setOnlineBonusTime(long time) {
        onlineBonusTime = time;
    }

    public void setRebirthRevive(boolean result) {
        rebirthRevive = result;
    }

    public boolean canUseRebirthRevive() {
        return rebirthRevive;
    }

    /**
     * Put up supplements to subtraction queue, so that when moving they would
     * not decrease, need update as confirmation To update use
     * updateSupplements()
     *
     * @param count
     * @param supplementId
     */
    public void subtractSupplements(int count, int supplementId) {
        subtractedSupplementsCount = count;
        subtractedSupplementId = supplementId;
    }

    /**
     * Update supplements in queue and clear the queue
     */
    public void updateSupplements() {
        if (subtractedSupplementId == 0 || subtractedSupplementsCount == 0) {
            return;
        }
        this.getInventory().decreaseByItemId(subtractedSupplementId, subtractedSupplementsCount);
        subtractedSupplementsCount = 0;
        subtractedSupplementId = 0;
    }

    public int getPortAnimation() {
        return portAnimation;
    }

    public void setPortAnimation(int portAnimation) {
        this.portAnimation = portAnimation;
    }

    @Override
    public boolean isSkillDisabled(SkillTemplate template) {
        ChainCondition cond = template.getChainCondition();
        if (cond != null && cond.getSelfCount() > 0) {// exception for multicast
            int chainCount = this.getChainSkills().getChainCount(this, template, cond.getCategory());
            if (chainCount > 0 && chainCount < cond.getSelfCount() && this.getChainSkills().chainSkillEnabled(cond.getCategory(), cond.getTime())) {
                return false;
            }
        }
        return super.isSkillDisabled(template);
    }

    /**
     * @return the houses
     */
    public List<House> getHouses() {
        if (houses == null) {
            List<House> found = HousingService.getInstance().searchPlayerHouses(this.getObjectId());
            if (found.size() > 0) {
                houses = found;
            } else {
                return found;
            }
        }
        return houses;
    }

    public void resetHouses() {
        if (houses != null) {
            houses.clear();
            houses = null;
        }
    }

    public House getActiveHouse() {
        for (House house : getHouses()) {
            if (house.getStatus() == HouseStatus.ACTIVE || house.getStatus() == HouseStatus.SELL_WAIT) {
                return house;
            }
        }

        return null;
    }

    public int getHouseOwnerId() {
        House house = getActiveHouse();
        if (house != null) {
            return house.getAddress().getId();
        }

        return 0;
    }

    public HouseRegistry getHouseRegistry() {
        return houseRegistry;
    }

    public void setHouseRegistry(HouseRegistry houseRegistry) {
        this.houseRegistry = houseRegistry;
    }

    public byte getBuildingOwnerStates() {
        return buildingOwnerStates;
    }

    public boolean isBuildingInState(PlayerHouseOwnerFlags state) {
        return (buildingOwnerStates & state.getId()) != 0;
    }

    public void setBuildingOwnerState(byte state) {
        buildingOwnerStates |= state;
        House house = getActiveHouse();
        if (house != null) {
            house.fixBuildingStates();
        }
    }

    public void unsetBuildingOwnerState(byte state) {
        buildingOwnerStates &= ~state;
        House house = getActiveHouse();
        if (house != null) {
            house.fixBuildingStates();
        }
    }

    public float[] getBattleReturnCoords() {
        return this.battleReturnCoords;
    }

    public void setBattleReturnCoords(int mapId, float[] coords) {
        this.battleReturnMap = mapId;
        this.battleReturnCoords = coords;
    }

    public int getBattleReturnMap() {
        return battleReturnMap;
    }

    public int getUseAutoGroup() {
        return useAutoGroup;
    }

    public boolean isUseAutoGroup() {
        return useAutoGroup == 1;
    }

    public void setUseAutoGroup(int useAutoGroup) {
        this.useAutoGroup = useAutoGroup;
    }

    public boolean isInSprintMode() {
        return isInSprintMode;
    }

    public void setSprintMode(boolean isInSprintMode) {
        this.isInSprintMode = isInSprintMode;
    }

    public void setRideObservers(ActionObserver observer) {
        if (rideObservers == null) {
            rideObservers = new ArrayList<>(3);
        }

        rideObservers.add(observer);
    }

    public List<ActionObserver> getRideObservers() {
        return rideObservers;
    }

    public InGameShop inGameShop() {
        return inGameShop;
    }

    public AbsoluteStatOwner getAbsoluteStats() {
        return absStatsHolder;
    }

    public WorldPosition getPrevPos() {
        if (getPosition() == null || !getPosition().isSpawned()) {
            return null;
        }
        if (prevPos == null || prevPos.getMapId() != getPosition().getMapId()) {
            prevPos = new WorldPosition(getPosition().getMapId());
            prevPos.setXYZH(getPosition().getX(), getPosition().getY(), getPosition().getZ(), getPosition().getHeading());
        }
        return prevPos;
    }

    @Override
    public WorldPosition getPosition() {
        return playerCommonData.getPosition();
    }

    @Override
    public void setPosition(WorldPosition position) {
        playerCommonData.setPosition(position);
    }

    // ========================[Aion-Light EMU]===========================
    public FastList<WorldBuff> getWorldBuffList() {
        return worldBuff;
    }

    public void addWorldBuff(WorldBuff buff) {
        if (worldBuff == null) {
            worldBuff = FastList.newInstance();
        }
        worldBuff.add(buff);
    }

    public PlayerBonusTime getBonusTime() {
        return bonusTime;
    }

    public void setBonusTime(PlayerBonusTime bonusTime) {
        this.bonusTime = bonusTime;
    }

    /**
     * @return getClientConnection().getAccount().getMembership()
     */
    public byte getMembership() {
        return mLevel;
    }

    /**
     * @return ip or mac - developers for fully access in game
     */
    public boolean isDeveloper() {
        return getPlayerAccount().isDeveloper();
    }

    public void addObsceneWordsCount() {
        owc++;
    }

    /**
     * @return ObsceneWordsCount
     */
    public int getObsceneWordsCount() {
        return owc;
    }

    /**
     * @return ObsceneWordsTime
     */
    public long getObsceneWordsTime() {
        return owt;
    }

    /**
     * @param time
     */
    public void setObsceneWordsTime(long time) {
        owt = time;
    }

    /**
     * @param count
     */
    public void setObsceneWordsCount(int count) {
        owc = count;
    }

    /**
     * @param GAGTimer
     */
    public void setGAGTimer(long GAGTimer) {
        if (GAGTimer < 0) {
            GAGTimer = 0;
        }

        this.GAGTimer = GAGTimer;
    }

    /**
     * @return GAGTimer
     */
    public long getGAGTimer() {
        return GAGTimer;
    }

    /**
     * @return startGAG
     */
    public long getStartGAG() {
        return startGAG;
    }

    /**
     * @param start
     */
    public void setStartGAG(long start) {
        this.startGAG = start;
    }

    /**
     * @return the isGagged
     */
    public boolean isGagged() {
        return GAGTimer != 0;
    }

    public void setPShopTimer(long privateShopTimer) {
        if (privateShopTimer < 0) {
            privateShopTimer = 0;
        }

        this.privateShopTimer = privateShopTimer;
    }

    public long getPShopTimer() {
        return privateShopTimer;
    }

    public long getStartPShop() {
        return startPrivateShop;
    }

    public void setStartPShop(long startPrivateShop) {
        this.startPrivateShop = startPrivateShop;
    }

    public boolean isBannedPStore() {
        return privateShopTimer != 0;
    }

    /**
     * @return
     */
    public TeleportTask getTeleportTask() {
        return teleportTask;
    }

    /**
     * @param teleportTask
     */
    public void setTeleportTask(TeleportTask teleportTask) {
        this.teleportTask = teleportTask;
    }

    /**
     * @param itemId
     * @param thisCount
     */
    public void addItemMaxCountOfDay(int itemId, int thisCount) {
        if (maxCountEvent == null) {
            maxCountEvent = new FastMap<Integer, MaxCountOfDay>().shared();
        }

        if (maxCountEvent.get(itemId) != null) {
            maxCountEvent.get(itemId).setThisCount(thisCount);
        } else {
            maxCountEvent.put(itemId, new MaxCountOfDay(thisCount));
        }
    }

    /**
     * @param itemId
     * @return maxCountEvent.get(itemId).getThisCount()
     */
    public int getItemMaxThisCount(int itemId) {
        if (maxCountEvent == null || !maxCountEvent.containsKey(itemId)) {
            return 0;
        }
        return maxCountEvent.get(itemId).getThisCount();
    }

    /**
     * @param itemId
     */
    public void removeItemMaxThisCount(int itemId) {
        if (maxCountEvent == null) {
            return;
        }
        maxCountEvent.remove(itemId);
    }

    public void clearItemMaxThisCount() {
        if (maxCountEvent == null) {
            return;
        }
        maxCountEvent.clear();
    }

    /**
     * @return the maxCountEvent
     */
    public Map<Integer, MaxCountOfDay> getItemMaxThisCounts() {
        return maxCountEvent;
    }

    public long getCreationDate() {
        Timestamp creationDate = playerCommonData.getCreationDate();
        if (creationDate == null) {
            return 0;
        }

        return creationDate.getTime();
    }

    public void setCreationDataDay(long i) {
        this.creationDay = i;
    }

    public long getCreationDataDay() {
        return creationDay;
    }

    public void setBonusTimeStatus() {
        Timestamp tm = getClientConnection().getAccount().getPlayerAccountData(getObjectId()).getPlayerCommonData().getLastOnline();
        long lastOnlineTimeDay = (System.currentTimeMillis() - tm.getTime()) / 24 / 60 / 60 / 1000;
        long t = (System.currentTimeMillis() - getCommonData().getCreationDate().getTime()) / 24 / 60 / 60 / 1000;
        long bonus_time = getBonusTime().getTime() != null ? System.currentTimeMillis() - getBonusTime().getTime().getTime() : 0;
        boolean bonus_comeback = System.currentTimeMillis() < bonus_time;
        setCreationDataDay(t);
        if (t <= 3L) {
            setNew(true);
        } else {
            setNew(false);
        }
        if (getBonusTime().getStatus() == PlayerBonusTimeStatus.COMEBACK && WellcomeToAtrea.returned_inventory && bonus_comeback
                || getBonusTime().getStatus() == PlayerBonusTimeStatus.NEW && WellcomeToAtrea.novice_inventory && t <= 30) {
            return;
        }

        if (WellcomeToAtrea.novice_inventory && t <= 30L) {
            getBonusTime().setStatus(PlayerBonusTimeStatus.NEW);
        } else if (WellcomeToAtrea.returned_inventory && lastOnlineTimeDay >= 30L) {
            getBonusTime().setStatus(PlayerBonusTimeStatus.COMEBACK);
            getBonusTime().setTime(new Timestamp(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        } else {
            getBonusTime().setStatus(PlayerBonusTimeStatus.NORMAL);
        }
    }

    public void setTeleportedTask(boolean tp) {
        this.tp = tp;
    }

    public boolean getTpTask() {
        return this.tp;
    }

    public int getEnterWorldCount() {
        return getCommonData().getEnterWorldCount();
    }

    public void setEnterWorldCount(int count) {
        getCommonData().setEnterWorldCount(count);
    }

    public void increaseNpcKills() {
        getCommonData().increaseNpcKills();
    }

    public int getNpcKills() {
        return getCommonData().getNpcKills();
    }

    public void increaseSpecialKillsMob() {
        getCommonData().increaseSpecialNpcKills();
    }

    public int getSpecialNpcKills() {
        return getCommonData().getSpecialNpcKills();
    }

    public boolean isInWorldNoFPReduce() {
        if (CustomConfig.ENABLE_UNLIMIT_FLY_LOC) {
            switch (getWorldId()) {
                case 710010000:
                case 700010000:
                case 110010000:
                case 120010000:
                    return true;
            }
        }
        return false;
    }

    public void increaseDuelWin() {
        getCommonData().increaseDuelWin();
    }

    public void increaseDuelLose() {
        getCommonData().increaseDuelLose();
    }

    public void putTempStorage(int index, Object obj) {
        tempStorage.put(index, obj);
    }

    public Object getTempStorage(int index) {
        return tempStorage.get(index);
    }

    public void clearTempStorage(int index) {
        tempStorage.remove(index);
    }

    public List<PlayerDropItem> getDrop() {
        return playerDrop;
    }

    public void addItems(PlayerDropItem item) {
        playerDrop.add(item);
    }

    public void setNew(boolean b) {
        this.newPlayer = b;
    }

    public boolean isNewPlayer() {
        return newPlayer;
    }

    public void increasePvPLocationKills() {
        this.pvplockills++;
    }

    public int getPvPLocKills() {
        return pvplockills;
    }

    public void setPvPLocationKills(int i) {
        this.pvplockills = i;
    }

    public void setGP(boolean b) {
        this.gp = b;
    }

    public boolean isGP() {
        return gp;
    }

    public void setCountHtmlGuides(int count) {
        this.countGuide = count;
    }

    public void increaseCountGuide() {
        this.countGuide++;
    }

    public void uncreaseCountGuide() {
        this.countGuide--;
    }

    public int getCountHtmlGuides() {
        return countGuide;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isWeater() {
        return this.water;
    }

    public void setSaveZonePosition(int worldId, int instanceId, float x, float y, float z) {
        saveWorld = worldId;
        saveInstance = instanceId;
        saveX = x;
        saveY = y;
        saveZ = z;
    }

    public void saveZonePosition() {
        saveWorld = getWorldId();
        saveInstance = getInstanceId();
        saveX = getX();
        saveY = getY();
        saveZ = getZ();
    }

    public int getSaveWorld() {
        return saveWorld;
    }

    public int getSaveInstance() {
        return saveInstance;
    }

    public float getSaveX() {
        return saveX;
    }

    public float getSaveY() {
        return saveY;
    }

    public float getSaveZ() {
        return saveZ;
    }

    public int getAccuseLevel() {
        return playerCommonData.getAccueseLevel();
    }

    public void setAccueseLevel(int lvl) {
        playerCommonData.setAccueseLevel(lvl);
    }

    public void increaseAccueseLevel() {
        playerCommonData.increaseAccuese();
    }

    public boolean addAccountIdAccuese(int accountId) {
        if (!accueseAccountId.contains(accountId)) {
            accueseAccountId.add(accountId);
            playerCommonData.increaseAccuese();
            //save db
            Timestamp time = new Timestamp(System.currentTimeMillis());
            MySQL5AionDAO.getInstance().insertAccuese(accountId, this.getObjectId(), time);
            return true;
        }
        return false;
    }

    public void loadAccuese(int aInt) {
        if (!accueseAccountId.contains(aInt)) {
            accueseAccountId.add(aInt);
        }
    }

    public List<Integer> getAccueseAccountIds() {
        return accueseAccountId;
    }

    public boolean isAccueseBan() {
        return accuese;
    }

    public void setAccueseBan(boolean acc) {
        this.accuese = acc;
    }

    public boolean isBanAttack() {
        return banAttack;
    }

    public void setBanAttack(boolean att) {
        banAttack = att;
    }

    public boolean isShowSpecialInGameShopList() {
        return specialShopList;
    }

    public void setSpecialShopList(boolean shop) {
        specialShopList = shop;
        saveShop = true;
    }

    public boolean isSaveShopSettings() {
        return saveShop;
    }

    public void setFishing(boolean b) {
        fishing = b;
    }

    public boolean isFishing() {
        return fishing;
    }

    public boolean isBanItems() {
        return banitems;
    }

    public void setBanItems(boolean it) {
        this.banitems = it;
    }

    public boolean isShowPacket() {
        return showPacket;
    }

    public void setShowPacket(boolean state) {
        showPacket = state;
    }
    private boolean gmteleporter = true;

    public boolean isGMTeleporter() {
        return gmteleporter;
    }

    public void setGMTeleporter(boolean set) {
        this.gmteleporter = set;
    }

    public void setPrisonLocation(int type) {
        if (type == 0) {
            type = -1;
        } else {
            type = Prison.getInstance().genLoc(getRace());
        }
        prisonLocation = type;
    }

    public int getPrisonLocation() {
        return prisonLocation;
    }

    //new
    public void setGShopTimer(long gameShopTimer) {
        if (privateShopTimer < 0) {
            privateShopTimer = 0;
        }

        this.gameShopTimer = gameShopTimer;
    }

    public long getGShopTimer() {
        return gameShopTimer;
    }

    public long getStartGShop() {
        return startGameShop;
    }

    public void setStartGShop(long startGameShop) {
        this.startGameShop = startGameShop;
    }

    public boolean isBannedGameShop() {
        return gameShopTimer != 0;
    }

    public long getTeleportTimer() {
        return teleportTimer;
    }

    public void setTeleportTimer(long teleportTimer) {
        this.teleportTimer = teleportTimer;
    }

    public long getStartTeleport() {
        return startTeleport;
    }

    public void setStartTeleport(long startTeleport) {
        this.startTeleport = startTeleport;
    }

    public boolean isTeleportBanned() {
        return teleportTimer != 0;
    }

    private String chatBanReason;
    private String prisonReason;
    private String privateStoreBanReason;
    private String gameShopBanReason;
    private String teleportBanReason;
    private String moveBanReason;

    public String getTeleportBanReason() {
        return teleportBanReason;
    }

    public void setTeleportBanReason(String teleportBanReason) {
        this.teleportBanReason = teleportBanReason;
    }

    public String getChatBanReason() {
        return chatBanReason;
    }

    public void setChatBanReason(String chatBanReason) {
        this.chatBanReason = chatBanReason;
    }

    public String getPrisonReason() {
        return prisonReason;
    }

    public void setPrisonReason(String prisonReason) {
        this.prisonReason = prisonReason;
    }

    public String getPrivateStoreBanReason() {
        return privateStoreBanReason;
    }

    public void setPrivateStoreBanReason(String privateStoreBanReason) {
        this.privateStoreBanReason = privateStoreBanReason;
    }

    public String getGameShopBanReason() {
        return gameShopBanReason;
    }

    public void setGameShopBanReason(String gameShopBanReason) {
        this.gameShopBanReason = gameShopBanReason;
    }

    public void setMoveBanReason(String reason) {
        this.moveBanReason = reason;
    }

    public String getMoveBanReason() {
        return moveBanReason;
    }

    public void setMoveTimer(long l) {
        moveTimer = l;
    }

    public long getMoveTimer() {
        return moveTimer;
    }

    public long getStartBanMove() {
        return startMove;
    }

    public void setStartBanMove(long l) {
        this.startMove = l;
    }

    public boolean isMoveBanned() {
        return moveTimer != 0;
    }

    public void setChatType(ChatType ct) {
        this.chatType = ct;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setCommandUsed(boolean b) {
        this.commandUsed = b;
    }

    public boolean isCommandUsed() {
        return this.commandUsed;
    }

    public void setTeleport(boolean b) {
        this.teleport = b;
    }

    public boolean isTeleport() {
        return this.teleport;
    }

    public void addStigmaTask() {
        stigmaCounter++;
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                stigmaCounter = 0;
            }
        }, 5 * 1000);//15 сек
    }
    private String customLegName = "";

    public void setLegName(String ln) {
        if (ln == null) {
            if (isGM()) {
                ln = AccessLevelEnum.getAlType(getAccessLevel()).getStatusName();
            } else {
                ln = getLegion() == null ? "" : getLegion().getLegionName();
            }
        }
        customLegName = ln;
    }

    public String getCustomLegionName() {
        return customLegName;
    }

    public void setNickerText(String text) {
        this.nicker = text;
    }

    public String getNickerText() {
        return nicker;
    }

    public void setPlayerSearch(String rename) {
        boolean marry = isMarried();
        String nf = nameFormat;
        if (rename == null) {
            if (marry) {
                nf = "\ue020 %s";
            }
            rename = String.format(nf, getName());
        }
        this.nameSearch = rename;
    }

    public String getPlayerSearch() {
        return nameSearch;
    }

    public void setNewName(String name) {
        if (name == null) {
            name = getName();
            if (wedding != null) {
                name += getWeddingsName();
            }
        }
        this.newname = name;
    }

    public String getNewName() {
        if (newname == null) {
            newname = getName();
        }
        return this.newname;
    }

    public void setNameFormat(String format) {
        if (format == null) {
            format = "%s";
            StringBuilder sb = new StringBuilder(nameFormat);
            int mem = getMembership();
            if (MembershipConfig.PREMIUM_TAG_DIPLAY && mem > 0 && mem < 3) {
                String ml = MembershipLevelEnum.getMlType(mem).getName();
                format = sb.insert(0, ml.substring(0, 2)).toString();
            }

            if (AdminConfig.CUSTOMTAG_ENABLE) {
                int level = getAccessLevel();
                if (level > 0/* && level < 8*/) {
                    String al = AccessLevelEnum.getAlType(level).getName();
                    format = sb.insert(0, al.substring(0, al.length() - 3)).toString();
                }
            }
        }
        this.nameFormat = format;
    }

    public String getNameFormat() {
        return this.nameFormat;
    }

    public boolean isEnterWorld() {
        return enterWorld;
    }

    public void enterWorld() {
        this.enterWorld = !enterWorld;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean log) {
        this.logged = log;
    }

    public boolean isVIP() {
        return getMembership() == 2;
    }

    public byte getCategoryShop() {
        return catShop;
    }

    public void setCategoryShop(byte n) {
        this.catShop = n;
    }

    public byte getSubCategoryShop() {
        return catSubShop;
    }

    public void setSubCategoryShop(byte n) {
        this.catSubShop = n;
    }

    private VisibleObject oldTarget;

    public VisibleObject getOldTarget() {
        return this.oldTarget;
    }

    public void setOldTarget(VisibleObject target) {
        oldTarget = target;
    }

    public void setRequestSenderList(RequestList loadSender) {
        this.loadSender = loadSender;
    }

    public RequestList getRequestSenderList() {
        return loadSender;
    }

    public AddItem addItem() {
        if (addItem == null) {
            addItem = new AddItem(this);
        }
        return this.addItem;
    }

    public void setGMSPY(int targetObjId) {
        targetId = targetObjId;
    }

    public int getGMSPYTargetId() {
        return targetId;
    }

    public void setAttacker(Player target) {
        this.attacker = target;
    }

    public Player getAttacker() {
        return attacker;
    }

    public void setWedding(Wedding wedding) {
        this.wedding = wedding;
    }

    public Wedding getWedding() {
        return wedding;
    }

    public String getWeddingsName() {
        return "\ue020" + wedding.getPartnerName();
    }

    public boolean isMarried() {
        return wedding != null;
    }

    public boolean isNoDeath() {
        return death;
    }

    public void setNoDeath(boolean s) {
        this.death = s;
    }

    public void addListItemLog(ItemLog il) {
        listItemLog.add(il);
    }

    public List<ItemLog> getListItemLog() {
        return listItemLog;
    }

    public void copy(Copy copy) {
        this.copy = copy;
    }

    public Copy getCopy() {
        return copy;
    }

    public boolean isInFfa() {
        return ffa;
    }

    public void setffa(boolean f) {
        this.ffa = f;
    }

    public void setPlayerGmPanel(Player player) {
        this.player = player;
    }

    public Player getPlayerGmPanel() {
        return this.player;
    }
}
