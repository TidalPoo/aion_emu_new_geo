/*
 * This file is part of aion-gates <aion-gates.fr>.
 *
 * aion-lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.world;

/**
 * @author Alex
 * @modified Jane
 */
public enum WorldMapType {

    // Asmodea
    PANDAEMONIUM(120010000, "Пандемониум", 10, 1500, "DC1"),
    MARCHUTAN(120020000, "Храм Маркутана", 0, 8000, "dc2"),
    ISHALGEN(220010000, "Исхальген", 10, 500, "DF1"),
    MORHEIM(220020000, "Морхейм", 30, 2500, "DF2"),
    ALTGARD(220030000, "Альтгард", 20, 1000, "DF1A"),
    BELUSLAN(220040000, "Белуслан", 40, 4500, "DF3"),
    BRUSTHONIN(220050000, "Брустхонин", 40, 8000, "DF2A"),
    // Elysia
    SANCTUM(110010000, "Элизиум", 10, 1500, "LC1"),
    KAISINEL(110020000, "Храм Кайсинель", 0, 8000, "lc2"),
    POETA(210010000, "Фоэта", 10, 500, "LF1"),
    ELTNEN(210020000, "Элтенен", 30, 2500, "LF2"),
    VERTERON(210030000, "Бертрон", 20, 1000, "LF1A"),
    HEIRON(210040000, "Интердика", 40, 4500, "LF3"),
    THEOBOMOS(210060000, "Теобомос", 40, 8000, "LF2A"),
    // Balaurea
    INGGISON(210050000, "Ингисон", 0, 11700, "LF4"),
    GELKMAROS(220070000, "Келькмарос", 0, 11700, "DF4"),
    SILENTERA_CANYON(600010000, "Тоннель Силентера", 0, 12800, "Underpass"),
    // Prison
    DE_PRISON(510010000, "Тюрьма Элийцев", "LF_Prison"), // For ELYOS
    DF_PRISON(520010000, "Тюрьма Асмодиан", "DF_Prison"), // For ASMODIANS

    RESHANTA(400010000, "Арэшурат", 1, 300, "AB1"), //attention teleport to reshanta - 300 АР
    // Instances
    NO_ZONE_NAME(300010000, "No Zone Name", "IDAbPro"),
    ID_TEST_DUNGEON(300020000, "IDTest_Dungeon", "IDTest_Dungeon"),
    NOCHSANA_TRAINING_CAMP(300030000, "Лагерь Насан", "IDAb1_MiniCastle"),
    DARK_POETA(300040000, "Фоэта тьмы", "IDLF1"),
    ASTERIA_CHAMBER(300050000, "Пропасть Астерии", "IDAbRe_Up_Asteria"),
    SULFUR_TREE_NEST(300060000, "Гнездо серного дерева", "IDAbRe_Low_Divine"),
    CHAMBER_OF_ROAH(300070000, "Руины подземной крепости Ру", "IDAbRe_Up_Rhoo"),
    LEFT_WING_CHAMBER(300080000, "Тень левого крыла", "IDAbRe_Low_Wciel"),
    RIGHT_WING_CHAMBER(300090000, "Тень правого крыла", "IDAbRe_Low_Eciel"),
    STEEL_RAKE(300100000, "Корабль Стальной плавник", "IDshulackShip"),
    DREDGION(300110000, "Дерадикон", "IDAb1_Dreadgion"),
    KYSIS_CHAMBER(300120000, "Крепость Ткисас", "IDAbRe_Up3_Dkisas"),
    MIREN_CHAMBER(300130000, "Крепость Ра-Мирэна", "IDAbRe_Up3_Lamiren"),
    KROTAN_CHAMBER(300140000, "Крепость Кротан", "IDAbRe_Up3_Crotan"),
    UDAS_TEMPLE(300150000, "Заброшенный храм Удас", "IDTemple_Up"),
    UDAS_TEMPLE_LOWER(300160000, "Подземелье храма Удас", "IDTemple_Low"),
    BESHMUNDIR_TEMPLE(300170000, "Храм Пхасумандир", "IDCatacombs"),
    TALOCS_HOLLOW(300190000, "Внутренняя часть Каспара", "IDElim"),
    HARAMEL(300200000, "Харамель", "IDNovice"),
    DREDGION_OF_CHANTRA(300210000, "Дерадикон Джантры", "IDDreadgion_02"),
    CORE(300220000, "Руины хаоса", "IDAbRe_Core"),
    CROMEDE(300230000, "Кошмар", "IDCromede"),
    KARAMATIS(310010000, "Караматис", "IDAbProL1"),
    KARAMATIS_B(310020000, "Караматис_B", "IDAbProL2"),
    AERDINA(310030000, "Аэрдина", "IDAbGateL1"),
    GERANAIA(310040000, "Геранайя", "IDAbGateL2"),
    AETHEROGENETICS_LAB(310050000, "Секретная лаборатория повстанцев", "IDLF3Lp"),
    FRAGMENT_OF_DARKNESS(310060000, "Остров тьмы", "IDLF1B"),
    IDLF1B_STIGMA(310070000, "Остров тьмы", "IDLF1B_Stigma"),
    SANCTUM_UNDERGROUND_ARENA(310080000, "Подземная арена Элизиума", "IDLC1_arena"),
    TRINIEL_UNDERGROUND_ARENA(320090000, "Подземная арена Триниель", "IDDC1_arena"),
    INDRATU_FORTRESS(310090000, "Крепость Индрата", "IDLF3_Castle_indratoo"),
    AZOTURAN_FORTRESS(310100000, "Крепость Аджотуран", "IDLF3_Castle_Lehpar"),
    THEOBOMOS_LAB(310110000, "Секретная лаборатория Теобомоса", "IDLF2a_Lab"),
    IDAB_PRO_L3(310120000, "IDAbProL3", "IDAbProL3"),
    ATAXIAR(320010000, "Нарзас", "IDAbProD1"),
    ATAXIAR_B(320020000, "Нарзас", "IDAbProD2"),
    BREGIRUN(320030000, "Брегирун", "IDAbGateD1"),
    NIDALBER(320040000, "Нидальбер", "IDAbGateD2"),
    ARKANIS_TEMPLE(320050000, "Храм Арканис", "IDDF2Flying"),
    SPACE_OF_OBLIVION(320060000, "Обитель забвения", "IDDF1B"),
    SPACE_OF_DESTINY(320070000, "Обитель судьбы", "IDSpace"),
    DRAUPNIR_CAVE(320080000, "Пещера Драубнир", "IDDF3_Dragon"),
    FIRE_TEMPLE(320100000, "Святилище огня", "IDDF2_Dflame"),
    ALQUIMIA_RESEARCH_CENTER(320110000, "Лаборатории Альквимии", "IDDF3_LP"),
    SHADOW_COURT_DUNGEON(320120000, "Темница Храма правосудия", "IDDC1_Arena_3F"),
    ADMA_STRONGHOLD(320130000, "Форт Адма", "IDDf2a_Adma"),
    IDAB_PRO_D3(320140000, "Нарзас", "IDAbProD3"),
    // Maps 2.5
    KAISINEL_ACADEMY(110070000, "Священная арена Кайсинель", "ARENA_L_LOBBY"),
    MARCHUTAN_PRIORY(120080000, "Священная арена Маркутана", "ARENA_D_LOBBY"),
    ESOTERRACE(300250000, "Арака", "IDF4Re_Drana"),
    EMPYREAN_CRUCIBLE(300300000, "1-я священная арена", "IDArena"),
    // Map 2.6
    CRUCIBLE_CHALLENGE(300320000, "2-я священная арена", "IDArena_Solo"),
    // Maps 2.7
    ARENA_OF_CHAOS(300350000, "Боевая арена хаоса", "IDArena_pvp01"),
    ARENA_OF_DISCIPLINE(300360000, "Боевая арена доблести", "IDArena_pvp02"),
    CHAOS_TRAINING_GROUNDS(300420000, "Тренировочный зал хаоса", "IDArena_pvp01_T"),
    DISCIPLINE_TRAINING_GROUNDS(300430000, "Тренировочный зал доблести", "IDArena_pvp02_T"),
    PADMARASHKA_CAVE(320150000, "Пещера Мариссы", "IDDramata_01"),
    // Test Map
    TEST_BASIC(900020000, "TestBasic", "Test_Basic"),
    TEST_SERVER(900030000, "TestServer", "Test_Server"),
    TEST_GIANTMONSTER(900100000, "Test_GiantMonster", "Test_GiantMonster"),
    HOUSING_BARRACK(900110000, "Housing_barrack", "Housing_barrack"),
    Region_housing(900130000, "Штаб-квартира легиона", "IDLDF5RE_test"),
    Advanced_Personal_Housing(900140000, "Личный дом", "Test_Kgw"),
    // Maps 3.0

    // Instances
    RAKSANG(300310000, "Тамарэс", "IDRaksha"),
    RENTUS_BASE(300280000, "База Рентаса", "IDYun"),
    ATURAN_SKY_FORTRESS(300240000, "Воздушная крепость Атурам", "IDStation"),
    ELEMENTIS_FOREST(300260000, "Лес Раттис", "IDElemental_1"),
    ARGENT_MANOR(300270000, "Резиденция Доркель", "IDElemental_2"),
    MUADA_TRENCHER(300380000, "Гнездо владыки песка", "IDLDF4A_Raid"),
    STEEL_RAKE_CABIN(300460000, "Пассажирский салон Стального плавника", "IDshulackShip_Solo"),
    TERATH_DREDGION(300440000, "Дерадикон Садха", "IDDreadgion_03"),
    // Map 3.5
    ARENA_OF_HARMONY(300450000, "Арена Покровительства", "IDArena_Team01"),
    SATRA_TREASURE_HOARD(300470000, "Секретное хранилище Ситры", "IDTiamat_Reward"),
    IDTiamat_Solo(300490000, "Кровавый трон", "IDTiamat_Solo"),
    IDTiamat_Israphel(300500000, "Святилище уединения", "IDTiamat_Israphel"), //Israphel's Space
    TIAMAT_STRONGHOLD(300510000, "Форт Тиамат", "IDTiamat_1"),
    DRAGON_LORDS_REFUGE(300520000, "Пристанище лорда балауров", "IDTiamat_2"), //Dragon Lord's Refuge instance
    IDArena_Glory(300550000, "Арена Славы", "IDArena_Glory"),
    IDDF2Flying_Event01(300560000, "Мавзолей Императоров Шиго", "IDDF2Flying_Event01"), //Sky Temple of Arkanis
    IDArena_Team01_T(300570000, "Тренировочная Арена Покровительства", "IDArena_Team01_T"),
    UNSTABLE_SPLINTER(300600000, "Разрушенные Руины хаоса", "IDAbRe_Core_02"), //Unstable Abyssal Splinter
    HEXWAY(300700000, "Тоннель предательства", "IDUnderpassRe"),
    // Instances 4.3 NA
    IDLDF5Re_02(300530000, "Лаборатория Идгеля", "IDLDF5Re_02"),
    IDLDF5b_TD(300540000, "Непреступный бастион", "IDLDF5b_TD"),
    IDLDF5Re_03(300580000, "Хранилище пустоты", "IDLDF5Re_03"),
    IDLDF5_Under_01(300590000, "Мост Йормуганда", "IDLDF5_Under_01"),
    IDRuneweapon(300800000, "Каталамадж", "IDRuneweapon"),
    IDRuneweapon_Q(300900000, "Каталамадж рунов", "IDRuneweapon_Q"),
    IDLDF5RE_solo_Q(301000000, "Экспериментальный блок", "IDLDF5RE_solo_Q"),
    IDShulack_Rose_Solo_01(301010000, "Пристань Стальной розы", "IDShulack_Rose_Solo_01"),
    IDShulack_Rose_Solo_02(301020000, "Пассажирский салон Стальной розы", "IDShulack_Rose_Solo_02"),
    IDShulack_Rose_01(301030000, "Пристань Стальной розы", "IDShulack_Rose_01"),
    IDShulack_Rose_02(301040000, "Пассажирский салон Стальной розы", "IDShulack_Rose_02"),
    IDShulack_Rose_03(301050000, "Палуба Стальной розы", "IDShulack_Rose_03"),
    IDArena_Team01_T2(301100000, "Тренировочная арена сплоченности", "IDArena_Team01_T2"),
    IDLDF5_Under_Rune(301110000, "Рунадиум", "IDLDF5_Under_Rune"),
    IDKamar(301120000, "Поле битвы Камар", "IDKamar"),
    HALL_OF_KNOWLEDGE(300480000, "Храм знаний", "IDLDF5RE_Solo"),
    //HALL_OF_KNOWLEDGE_LEGION(301190000, "Храм знаний", "IDLDF5RE_Solo_L"),
    IDVritra_Base(301130000, "Военная база Сауро", "IDVritra_Base"),
    IDLDF5_Under_02(301140000, "Прибежище рунов", "IDLDF5_Under_02"),
    IDAsteria_IU_Party(301160000, "Цирк Лукибуки", "IDAsteria_IU_Party"),
    IDLDF5RE_02_L(301170000, "Лаборатория Идгеля (легион)", "IDLDF5Re_02_L"),
    IDLDF5RE_03_L(301180000, "Хранилище пустоты (легион)", "IDLDF5Re_03_L"),
    IDLDF5RE_Solo_L(301190000, "Храм знаний", "IDLDF5RE_Solo_L"),
    IDAsteria_IU_World(301200000, "Храм заний (легион)", "IDAsteria_IU_World"),
    OPHIDAN_BRIDGE(301210000, "", ""),// no such
    // Maps 4.3 NA
    NORHTERN_KATALAM(600050000, "Северный Каталам", 66, 49200, "LDF5a"),
    SOUTHERN_KATALAM(600060000, "Южный Каталам", 66, 52300, "LDF5b"),
    UNDERGROUND_KATALAM(600070000, "Подземный Каталам", 66, 47500, "LDF5_Under"),
    IDIU(600080000, "Зал для вечеринок", "IDIU"),
    // Housing
    ORIEL(700010000, "Элиан", 15, 1500, "Housing_LF_personal"),
    PERNON(710010000, "Фернон", 15, 1500, "Housing_DF_personal"),
    // Maps
    SARPAN(600020000, "Сарфан", "LDF4a"),
    SARPAN_SKY(300410000, "Sarpan sky", 16550, "IDLDF4a_Intro"),
    TIAMARANTA(600030000, "Тиамаранта", 0, 36000, "LDF4b"),
    TIAMARANTA_EYE(300400000, "Око Тиамаранты", 0, 52000, "IDLDF4b_Tiamat"),
    TIAMARANTA_EYE_2(600040000, "Око Тиамаранты", 0, 52000, "Tiamat_Down"),
    // Others
    PROTECTOR_REALM(300330000, "Комната хранителя реликвий", "IDLDF4A"),
    ISRAPHEL_TRACT(300390000, "Чертог Израфеля", "IDLDF4A_Lehpar"),
    HOUSING_LC_LEGION(700020000, true, "Штаб легиона Элийцев", "Housing_LC_legion"),
    HOUSING_DC_LEGION(710020000, true, "Штаб легиона Асмодеан", "Housing_DC_legion"),
    HOUSING_IDLF_PERSONAL(720010000, true, "Квартира Элийцев", "housing_idlf_personal"),
    HOUSING_IDDF_PERSONAL(730010000, true, "Квартира Асмодеан", "housing_iddf_personal"),
    SEARCH_TESTER(0, "No such", "No such");

    private final int worldId;
    private final boolean isPersonal;
    private final String rusname;
    private final int flyLevel;
    private final int price;
    private String clientName;

    WorldMapType(int worldId, boolean ss, String rusname, String clientName) {
        this(worldId, ss, rusname, 0, 0, clientName);
    }

    WorldMapType(int worldId, String rusname, int price, String clientName) {
        this(worldId, false, rusname, 60, price, clientName);
    }

    WorldMapType(int worldId, String rusname, int flyLevel, int price, String clientName) {
        this(worldId, false, rusname, flyLevel, price, clientName);
    }

    WorldMapType(int worldId, String rusname, String clientName) {
        this(worldId, false, rusname, 66, 0, clientName);
    }

    WorldMapType(int worldId, boolean personal, String rusname, int flyLevel, int price, String clientName) {
        this.worldId = worldId;
        this.isPersonal = personal;
        this.rusname = rusname;
        this.flyLevel = flyLevel;
        this.price = price;
        this.clientName = clientName;
    }

    public int getFlyLevel() {
        return flyLevel;
    }

    public String getRusname() {
        return rusname;
    }

    public int getId() {
        return worldId;
    }

    public boolean isPersonal() {
        return isPersonal;
    }

    public int getPrice() {
        return price;
    }

    public int getWorldId() {
        return worldId;
    }

    public String getClientName() {
        return clientName;
    }

    /**
     * @param id of world
     * @return WorldMapType
     */
    public static WorldMapType getWorld(int id) {
        for (WorldMapType type : WorldMapType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public static WorldMapType getWorldClientName(String name) {
        for (WorldMapType type : WorldMapType.values()) {
            if (type.getClientName().toLowerCase().equals(name)) {
                return type;
            }
        }
        return SEARCH_TESTER;
    }
}
