package by.righttwixys.isotopix.init;

import by.righttwixys.isotopix.api.ItemRegistryApi;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;

import static by.righttwixys.isotopix.api.ItemRegistryApi.ITEMS;

public class ModItems {

    public static DeferredItem<Item> DOSIMETER;

    public static void init() {
        DOSIMETER = ITEMS.registerItem("dosimeter", by.righttwixys.isotopix.item.DosimeterItem::new, new Item.Properties().stacksTo(1));

        // =========================================================================
        // 1. СТАБИЛЬНЫЕ И ЛЁГКИЕ ИЗОТОПЫ
        // =========================================================================
        addItem("h1",
                "Protium (1H)\nStable light hydrogen isotope.\nPrimary constituent of water and stellar matter.",
                "Протий (1H)\nСтабильный лёгкий изотоп водорода.\nОснова воды и звёздного вещества Вселенной.");

        addItem("h2",
                "Deuterium (2H)\nStable heavy hydrogen isotope.\nUsed as a moderator in heavy water reactors (CANDU) and fusion fuel.",
                "Дейтерий (2H)\nСтабильный тяжёлый водород.\nЗамедлитель в тяжеловодных реакторах и топливо термоядерного синтеза.");

        addItem("h3",
                "Tritium (3H)\nDecay: Beta- (18.6 keV)\nHalf-life: 12.32 years\nUsed in thermonuclear fusion and self-powered lighting.",
                "Тритий (3H)\nРаспад: Бета- (18.6 кэВ)\nПериод полураспада: 12.32 лет\nПрименяется в термоядерном синтезе и тритиевой подсветке.");

        addItem("he3",
                "Helium-3 (3He)\nStable light helium isotope.\nExtremely rare fusion fuel and neutron detector gas.",
                "Гелий-3 (3He)\nСтабильный лёгкий изотоп гелия.\nПерспективное термоядерное топливо и газ пропорциональных счетчиков нейтронов.");

        addItem("he4",
                "Helium-4 (4He)\nStable alpha-particle nucleus.\nSuperfluid at low temperatures and reactor cooling gas.",
                "Гелий-4 (4He)\nСтабильный изотоп гелия, ядро альфа-частицы.\nТеплоноситель высокотемпературных газоохлаждаемых реакторов.");

        addItem("li6",
                "Lithium-6 (6Li)\nStable alkali isotope (7.5% natural).\nTritium breeding material via neutron capture in thermonuclear devices.",
                "Литий-6 (6Li)\nСтабильный изотоп лития.\nСырьё для реакторной наработки трития при нейтронном захвате.");

        addItem("li7",
                "Lithium-7 (7Li)\nStable alkali isotope (92.5% natural).\npH-buffer in pressurized water reactors (PWR).",
                "Литий-7 (7Li)\nСтабильный изотоп лития.\nРегулятор водно-химического режима первого контура ВВЭР/PWR.");

        addItem("be7",
                "Beryllium-7 (7Be)\nDecay: Electron Capture, Gamma (478 keV)\nHalf-life: 53.22 days\nCosmogenic isotope formed in the upper atmosphere.",
                "Бериллий-7 (7Be)\nРаспад: Электронный захват, Гамма (478 кэВ)\nПериод полураспада: 53.22 суток\nКосмогенный нуклид, образующийся в верхних слоях стратосферы.");

        addItem("be9",
                "Beryllium-9 (9Be)\nStable light neutron reflector.\nLow neutron absorption cross section used in nuclear physics.",
                "Бериллий-9 (9Be)\nЕдинственный стабильный изотоп бериллия.\nВысокоэффективный отражатель и замедлитель нейтронов.");

        addItem("be10",
                "Beryllium-10 (10Be)\nDecay: Beta-\nHalf-life: 1.39 million years\nCosmogenic isotope used for geological dating.",
                "Бериллий-10 (10Be)\nРаспад: Бета-\nПериод полураспада: 1.39 млн лет\nКосмогенный нуклид, применяется в геохронологии.");

        addItem("b10",
                "Boron-10 (10B)\nStable isotope (19.9% natural).\nGigantic neutron capture cross-section (3,840 barns); reactor emergency shutdown agent.",
                "Бор-10 (10B)\nСтабильный поглотитель нейтронов (3,840 барн).\nОснова стержней аварийной защиты (АЗ) ядерных реакторов.");

        addItem("b11",
                "Boron-11 (11B)\nStable boron isotope (80.1% natural).\nHigh-temperature semiconductor component.",
                "Бор-11 (11B)\nСтабильный природный изотоп бора (80.1%).\nКомпонент радиационно-стойкой полупроводниковой электроники.");

        addItem("c12",
                "Carbon-12 (12C)\nStandard atomic mass unit isotope (12.000 u).\nHigh-purity reactor graphite moderator.",
                "Углерод-12 (12C)\nСтандарт шкалы атомных масс.\nОснова сверхчистого ядерного графита РБМК и AGR.");

        addItem("c13",
                "Carbon-13 (13C)\nStable carbon isotope (1.1% natural).\nKey reagent for NMR spectroscopy.",
                "Углерод-13 (13C)\nСтабильный природный изотоп углерода.\nСпектроскопический стандарт ядерного магнитного резонанса (ЯМР).");

        addItem("c14",
                "Carbon-14 (14C)\nDecay: Beta- (156 keV)\nHalf-life: 5,730 years\nEssential for radiocarbon archaeological dating.",
                "Углерод-14 (14C)\nРаспад: Бета- (156 кэВ)\nПериод полураспада: 5,730 лет\nОснова радиоуглеродного анализа в археологии.");

        addItem("n14",
                "Nitrogen-14 (14N)\nStable diatomic atmospheric gas (99.63%).\nParent nucleus of Carbon-14 via cosmic neutron capture.",
                "Азот-14 (14N)\nСтабильный природный газ атмосферы.\nМишень космических нейтронов для непрерывного синтеза Углерода-14.");

        addItem("n15",
                "Nitrogen-15 (15N)\nStable rare nitrogen isotope.\nNon-radioactive tracer for biochemical protein research.",
                "Азот-15 (15N)\nСтабильный редкий изотоп азота.\nНерадиоактивная метка для изучения синтеза аминокислот.");

        addItem("o16",
                "Oxygen-16 (16O)\nDominant stable atmospheric isotope (99.76%).\nPrimary component of reactor water coolant.",
                "Кислород-16 (16O)\nОсновной стабильный изотоп кислорода.\nОснова теплоносителя первого и второго контуров АЭС.");

        addItem("o18",
                "Oxygen-18 (18O)\nStable heavy water component.\nCyclotron target for synthesizing Fluorine-18.",
                "Кислород-18 (18O)\nСтабильный тяжёлый кислород.\nМишень циклотрона для наработки радиофармпрепарата Фтора-18.");

        addItem("f18",
                "Fluorine-18 (18F)\nDecay: Positron (Beta+) / EC\nHalf-life: 109.77 minutes\nThe gold standard for Positron Emission Tomography (FDG-PET).",
                "Фтор-18 (18F)\nРаспад: Позитрон (Бета+) / ЭЗ\nПериод полураспада: 109.77 минут\nМировой стандарт позитронно-эмиссионной томографии (ПЭТ/КТ).");

        addItem("na22",
                "Sodium-22 (22Na)\nDecay: Beta+ / Electron Capture\nHalf-life: 2.60 years\nPositron emitter used for PET calibration.",
                "Натрий-22 (22Na)\nРаспад: Бета+ / Электронный захват\nПериод полураспада: 2.60 года\nИсточник позитронов для калибровки ПЭТ-томографов.");

        addItem("na24",
                "Sodium-24 (24Na)\nDecay: Beta- (1.39 MeV), Hard Gamma (2.75 MeV)\nHalf-life: 14.96 hours\nActivation product in liquid sodium-cooled fast reactors (BN-800).",
                "Натрий-24 (24Na)\nРаспад: Бета- (1.39 МэВ), Гамма (2.75 МэВ)\nПериод полураспада: 14.96 часов\nПродукт активации теплоносителя быстрых реакторов (БН-600/800).");

        addItem("al26",
                "Aluminium-26 (26Al)\nDecay: Beta+ (1.17 MeV), Gamma (1.81 MeV)\nHalf-life: 717,000 years\nGalactic nucleosynthesis marker found in meteorites.",
                "Алюминий-26 (26Al)\nРаспад: Бета+ (1.17 МэВ), Гамма (1.81 МэВ)\nПериод полураспада: 717 тыс. лет\nКосмогенный хронометр, маркер звёздного нуклеосинтеза в метеоритах.");

        addItem("al28",
                "Aluminium-28 (28Al)\nDecay: Beta- (2.86 MeV), Gamma (1.78 MeV)\nHalf-life: 2.24 minutes\nShort-lived activation product used in Neutron Activation Analysis (NAA).",
                "Алюминий-28 (28Al)\nРаспад: Бета- (2.86 МэВ), Гамма (1.78 МэВ)\nПериод полураспада: 2.24 минуты\nПродукт нейтронной активации алюмосиликатных пород.");

        addItem("si32",
                "Silicon-32 (32Si)\nDecay: Beta- (224 keV)\nHalf-life: 153 years\nOceanographic tracer for silica deposition rates.",
                "Кремний-32 (32Si)\nРаспад: Бета- (224 кэВ)\nПериод полураспада: 153 года\nКосмогенный радиоизотоп для датирования донных отложений кремния.");

        addItem("p32",
                "Phosphorus-32 (32P)\nDecay: Pure Beta- (1.71 MeV)\nHalf-life: 14.29 days\nUsed in biochemistry, genetics, and oncology.",
                "Фосфор-32 (32P)\nРаспад: Чистый Бета- (1.71 МэВ)\nПериод полураспада: 14.29 суток\nИспользуется в молекулярной биологии и радиотерапии.");

        addItem("s35",
                "Sulfur-35 (35S)\nDecay: Low-energy Beta- (167 keV)\nHalf-life: 87.5 days\nEssential metabolic tracer for labeling methionine and proteins.",
                "Сера-35 (35S)\nРаспад: Мягкий Бета- (167 кэВ)\nПериод полураспада: 87.5 суток\nБиологический маркер для мечения белков и нуклеиновых кислот.");

        addItem("cl36",
                "Chlorine-36 (36Cl)\nDecay: Beta- / Electron Capture\nHalf-life: 301,000 years\nHydrological tracer for dating ancient groundwater.",
                "Хлор-36 (36Cl)\nРаспад: Бета- / Электронный захват\nПериод полураспада: 301 тыс. лет\nТрассер для датирования глубоких подземных вод.");

        addItem("ar39",
                "Argon-39 (39Ar)\nDecay: Beta- (565 keV)\nHalf-life: 269 years\nCosmogenic noble gas tracer for deep ocean circulation.",
                "Аргон-39 (39Ar)\nРаспад: Бета- (565 кэВ)\nПериод полураспада: 269 лет\nКосмогенный инертный газ, трассер глубоководной циркуляции океанов.");

        addItem("k40",
                "Potassium-40 (40K)\nDecay: Beta- (89%) / EC & Gamma (11%)\nHalf-life: 1.25 billion years\nPrimary source of natural radioactivity in biological organisms.",
                "Калий-40 (40K)\nРаспад: Бета- (89%) / ЭЗ и Гамма (11%)\nПериод полураспада: 1.25 млрд лет\nГлавный источник естественной радиоактивности живых организмов.");

        addItem("ca45",
                "Calcium-45 (45Ca)\nDecay: Beta- (257 keV)\nHalf-life: 162.6 days\nRadiotracer for studying bone metabolism.",
                "Кальций-45 (45Ca)\nРаспад: Бета- (257 кэВ)\nПериод полураспада: 162.6 суток\nРадиоактивная метка для изучения клеточного метаболизма кальция.");

        // =========================================================================
        // 2. ПРОМЫШЛЕННЫЕ И МЕДИЦИНСКИЕ РАДИОИЗОТОПЫ
        // =========================================================================
        addItem("sc46",
                "Scandium-46 (46Sc)\nDecay: Beta-, Gamma\nHalf-life: 83.79 days\nIndustrial tracer for petrochemical pipelines.",
                "Скандий-46 (46Sc)\nРаспад: Бета-, Гамма\nПериод полураспада: 83.79 суток\nПромышленный трассер для мониторинга нефтепроводов.");

        addItem("ti44",
                "Titanium-44 (44Ti)\nDecay: Electron Capture, Gamma\nHalf-life: 60.0 years\nLong-lived supernova nucleosynthesis marker.",
                "Титан-44 (44Ti)\nРаспад: Электронный захват, Гамма\nПериод полураспада: 60.0 лет\nДолгоживущий радиоизотоп вспышек сверхновых звёзд.");

        addItem("v48",
                "Vanadium-48 (48V)\nDecay: Beta+ / EC, High-energy Gamma\nHalf-life: 15.97 days\nUsed for wear-testing engine parts in tribology.",
                "Ванадий-48 (48V)\nРаспад: Бета+ / ЭЗ, Жесткая Гамма\nПериод полураспада: 15.97 суток\nПрименяется для радиоизотопного контроля износа двигателей.");

        addItem("cr51",
                "Chromium-51 (51Cr)\nDecay: Electron Capture, Gamma (320 keV)\nHalf-life: 27.7 days\nUsed in medicine to measure red blood cell volume.",
                "Хром-51 (51Cr)\nРаспад: Электронный захват, Гамма (320 кэВ)\nПериод полураспада: 27.7 суток\nПрименяется в гематологии для маркировки эритроцитов.");

        addItem("mn54",
                "Manganese-54 (54Mn)\nDecay: Electron Capture, Gamma (835 keV)\nHalf-life: 312.2 days\nFormed via neutron activation of structural steels.",
                "Марганец-54 (54Mn)\nРаспад: Электронный захват, Гамма (835 кэВ)\nПериод полураспада: 312.2 суток\nПродукт активации конструкционной стали в ядерных реакторах.");

        addItem("fe55",
                "Iron-55 (55Fe)\nDecay: Electron Capture (X-ray source)\nHalf-life: 2.73 years\nUsed in X-ray fluorescence analysis.",
                "Железо-55 (55Fe)\nРаспад: Электронный захват (источник рентгена)\nПериод полураспада: 2.73 года\nПрименяется в рентгенофлуоресцентных спектрометрах.");

        addItem("fe56",
                "Iron-56 (56Fe)\nMost stable nuclear binding configuration.\nEnd product of stellar nucleosynthesis.",
                "Железо-56 (56Fe)\nРекордная удельная энергия связи нуклонов.\nКонечный продукт термоядерных реакций в недрах звёзд.");

        addItem("fe59",
                "Iron-59 (59Fe)\nDecay: Beta-, Gamma\nHalf-life: 44.5 days\nMedical tracer for studying iron metabolism in liver and blood.",
                "Железо-59 (59Fe)\nРаспад: Бета-, Гамма\nПериод полураспада: 44.5 суток\nМедицинский маркер метаболизма железа в крови.");

        addItem("co57",
                "Cobalt-57 (57Co)\nDecay: Electron Capture, Gamma (122 keV)\nHalf-life: 271.7 days\nStandard calibration source for gamma cameras.",
                "Кобальт-57 (57Co)\nРаспад: Электронный захват, Гамма (122 кэВ)\nПериод полураспада: 271.7 суток\nКалибровочный источник для медицинских гамма-камер.");

        addItem("co59",
                "Cobalt-59 (59Co)\nOnly stable natural cobalt isotope.\nHigh thermal neutron capture cross-section yielding Cobalt-60.",
                "Кобальт-59 (59Co)\nЕдинственный стабильный природный изотоп кобальта.\nМишень для радиационного захвата нейтронов с получением Кобальта-60.");

        addItem("co60",
                "Cobalt-60 (60Co)\nDecay: Beta-, High-energy Gamma (1.17 & 1.33 MeV)\nHalf-life: 5.27 years\nIndustrial gamma defectoscopy and medical radiation therapy.",
                "Кобальт-60 (60Co)\nРаспад: Бета-, Мощная Гамма (1.17 и 1.33 МэВ)\nПериод полураспада: 5.27 лет\nПромышленная дефектоскопия, стерилизация и лучевая терапия.");

        addItem("co252",
                "Californium-252 / Isotope CO-252\nDecay: Alpha / Spontaneous Fission\nExtremely intense neutron emitter.",
                "Изотоп CO-252 / Калифорний-252\nРаспад: Альфа / Спонтанное деление\nСверхмощный компактный источник нейтронов.");

        addItem("ni63",
                "Nickel-63 (63Ni)\nDecay: Pure Beta- (67 keV)\nHalf-life: 100.1 years\nIdeal power source for betavoltaic nuclear batteries.",
                "Никель-63 (63Ni)\nРаспад: Чистый Бета- (67 кэВ)\nПериод полураспада: 100.1 лет\nИдеальный источник питания для бетавольтаических атомных батарей.");

        addItem("cu64",
                "Copper-64 (64Cu)\nDecay: Beta+ (18%) / Beta- (38%) / EC (44%)\nHalf-life: 12.70 hours\nTheranostic radioisotope combining PET imaging with radiotherapy.",
                "Медь-64 (64Cu)\nРаспад: Бета+ (18%) / Бета- (38%) / ЭЗ (44%)\nПериод полураспада: 12.70 часов\nТераноparamстический изотоп: одновременная ПЭТ-диагностика и терапия рака.");

        addItem("zn65",
                "Zinc-65 (65Zn)\nDecay: Positron / Electron Capture, Gamma (1.11 MeV)\nHalf-life: 244 days\nHeavy metal environmental radiotracer.",
                "Цинк-65 (65Zn)\nРаспад: Бета+ / Электронный захват, Гамма (1.11 МэВ)\nПериод полураспада: 244 суток\nИндикатор миграции тяжёлых металлов в гидросфере.");

        addItem("ga67",
                "Gallium-67 (67Ga)\nDecay: Electron Capture, Gamma\nHalf-life: 3.26 days\nRadiopharmaceutical for scintigraphy and tumor imaging.",
                "Галлий-67 (67Ga)\nРаспад: Электронный захват, Гамма\nПериод полураспада: 3.26 суток\nРадиофармпрепарат для сцинтиграфии воспалений и опухолей.");

        addItem("ga68",
                "Gallium-68 (68Ga)\nDecay: Positron (89%), Electron Capture\nHalf-life: 67.7 minutes\nKey PET isotope eluted from portable Ge-68 generators.",
                "Галлий-68 (68Ga)\nРаспад: Позитрон (89%), Электронный захват\nПериод полураспада: 67.7 минут\nКлючевой генераторный ПЭТ-изотоп для диагностики опухолей простаты.");

        addItem("ge68",
                "Germanium-68 (68Ge)\nDecay: Electron Capture (Pure)\nHalf-life: 270.9 days\nParent isotope in portable Ga-68 positron generators.",
                "Германий-68 (68Ge)\nРаспад: Чистый электронный захват\nПериод полураспада: 270.9 суток\nМатеринский изотоп для генераторов позитронного Галлия-68.");

        addItem("se75",
                "Selenium-75 (75Se)\nDecay: Electron Capture, Gamma\nHalf-life: 119.8 days\nNon-destructive gamma testing of pipeline welds.",
                "Селен-75 (75Se)\nРаспад: Электронный захват, Гамма\nПериод полураспада: 119.8 суток\nНеразрушающий радиографический контроль сварных швов труб.");

        addItem("br82",
                "Bromine-82 (82Br)\nDecay: Beta-, Multigamma\nHalf-life: 35.3 hours\nIndustrial hydrological and ventilation tracer.",
                "Бром-82 (82Br)\nРаспад: Бета-, Сложный Гамма-спектр\nПериод полураспада: 35.3 часов\nПромышленный индикатор герметичности вентиляционных шахт.");

        addItem("kr85",
                "Krypton-85 (85Kr)\nDecay: Beta- (687 keV), Gamma\nHalf-life: 10.75 years\nGaseous fission product used in hermetic leak detection.",
                "Криптон-85 (85Kr)\nРаспад: Бета- (687 кэВ), Гамма\nПериод полураспада: 10.75 лет\nГазообразный продукт деления, индикатор утечек герметичных корпусов.");

        addItem("rb87",
                "Rubidium-87 (87Rb)\nDecay: Beta- (282 keV)\nHalf-life: 49.7 billion years\nPrimordial radionuclide used in Rubidium-Strontium geochronology.",
                "Рубидий-87 (87Rb)\nРаспад: Бета- (282 кэВ)\nПериод полураспада: 49.7 млрд лет\nРеликтовый нуклид для рубидий-стронциевого датирования древнейших пород.");

        // =========================================================================
        // 3. ПРОДУКТЫ ДЕЛЕНИЯ ЯДЕРНОГО ТОПЛИВА (ОЯТ)
        // =========================================================================
        addItem("sr89",
                "Strontium-89 (89Sr)\nDecay: Pure Beta- (1.49 MeV)\nHalf-life: 50.57 days\nPalliative radiation therapy for metastatic bone cancer.",
                "Стронций-89 (89Sr)\nРаспад: Чистый Бета- (1.49 МэВ)\nПериод полураспада: 50.57 суток\nТерапия болевых синдромов при костных метастазах.");

        addItem("sr90",
                "Strontium-90 (90Sr)\nDecay: Beta- (546 keV) -> Y-90\nHalf-life: 28.9 years\nMajor long-lived nuclear waste fission product and RTG fuel.",
                "Стронций-90 (90Sr)\nРаспад: Бета- (546 кэВ) -> Y-90\nПериод полураспада: 28.9 лет\nКлючевой долгоживущий компонент ОЯТ и топливо наземных РИТЭГ.");

        addItem("y90",
                "Yttrium-90 (90Y)\nDecay: Pure Beta- (2.28 MeV)\nHalf-life: 64 hours\nDaughter of Sr-90 used in targeted radioimmunotherapy.",
                "Иттрий-90 (90Y)\nРаспад: Чистый Бета- (2.28 МэВ)\nПериод полураспада: 64 часа\nДочерний изотоп Sr-90 для таргетной радиоиммунотерапии.");

        addItem("zr93",
                "Zirconium-93 (93Zr)\nDecay: Beta- (60 keV)\nHalf-life: 1.53 million years\nUltra long-lived radioactive cladding activation waste.",
                "Цирконий-93 (93Zr)\nРаспад: Бета- (60 кэВ)\nПериод полураспада: 1.53 млн лет\nДолгоживущий радиоактивный отход реакторных циркониевых оболочек.");

        addItem("zr95",
                "Zirconium-95 (95Zr)\nDecay: Beta-, Gamma\nHalf-life: 64.02 days\nShort-lived reactor fuel burnup indicator.",
                "Цирконий-95 (95Zr)\nРаспад: Бета-, Гамма\nПериод полураспада: 64.02 суток\nИндикатор глубины выгорания свежевыгруженного ядерного топлива.");

        addItem("nb95",
                "Niobium-95 (95Nb)\nDecay: Beta-, Gamma (766 keV)\nHalf-life: 34.99 days\nDaughter of Zr-95 found in spent nuclear fuel pools.",
                "Ниобий-95 (95Nb)\nРаспад: Бета-, Гамма (766 кэВ)\nПериод полураспада: 34.99 суток\nДочерний нуклид циркония-95 в бассейнах выдержки ОЯТ.");

        addItem("mo99",
                "Molybdenum-99 (99Mo)\nDecay: Beta-, Gamma\nHalf-life: 65.94 hours\nCrucial parent isotope that decays into Technetium-99m.",
                "Молибден-99 (99Mo)\nРаспад: Бета-, Гамма\nПериод полураспада: 65.94 часа\nВажнейший изотоп, распадающийся в диагностический Технеций-99m.");

        addItem("tc99m",
                "Technetium-99m (99mTc)\nDecay: Isomeric Transition, Gamma (140 keV)\nHalf-life: 6.01 hours\nThe workhorse of global diagnostic nuclear medicine.",
                "Технеций-99m (99mTc)\nРаспад: Изомерный переход, Гамма (140 кэВ)\nПериод полураспада: 6.01 часов\nГлавный диагностический радиофармпрепарат в мировой медицине.");

        addItem("tc99",
                "Technetium-99 (99Tc)\nDecay: Beta- (294 keV)\nHalf-life: 211,100 years\nLong-lived fission product target for nuclear transmutation.",
                "Технеций-99 (99Tc)\nРаспад: Бета- (294 кэВ)\nПериод полураспада: 211,100 лет\nДолгоживущий осколок деления, цель для ядерной трансмутации.");

        addItem("ru103",
                "Ruthenium-103 (103Ru)\nDecay: Beta-, Gamma\nHalf-life: 39.26 days\nFission product used to monitor reactor releases.",
                "Рутений-103 (103Ru)\nРаспад: Бета-, Гамма\nПериод полураспада: 39.26 суток\nПродукт деления, используемый для анализа аварийных выбросов АЭС.");

        addItem("ru106",
                "Ruthenium-106 (106Ru)\nDecay: Beta- -> Rh-106 (3.54 MeV beta)\nHalf-life: 373.6 days\nUsed in ophthalmic eye applicators for melanoma treatment.",
                "Рутений-106 (106Ru)\nРаспад: Бета- -> Rh-106 (3.54 МэВ)\nПериод полураспада: 373.6 суток\nПрименяется в глазных радиотерапевтических аппликаторах.");

        addItem("pd103",
                "Palladium-103 (103Pd)\nDecay: Electron Capture, X-ray (20 keV)\nHalf-life: 16.99 days\nBrachytherapy seed implant for prostate cancer.",
                "Палладий-103 (103Pd)\nРаспад: Электронный захват, Рентген (20 кэВ)\nПериод полураспада: 16.99 суток\nМикроисточник для брахитерапии онкологических заболеваний.");

        addItem("pd107",
                "Palladium-107 (107Pd)\nDecay: Pure Beta- (33 keV)\nHalf-life: 6.5 million years\nUltra long-lived low-energy fission product in nuclear waste.",
                "Палладий-107 (107Pd)\nРаспад: Чистый Бета- (33 кэВ)\nПериод полураспада: 6.5 млн лет\nСверхдолгоживущий компонент отходов радиохимической переработки ОЯТ.");

        addItem("ag110m",
                "Silver-110m (110mAg)\nDecay: Beta-, High-energy Gamma\nHalf-life: 249.8 days\nPrimary activation product in reactor cooling loops.",
                "Серебро-110m (110mAg)\nРаспад: Бета-, Жесткая Гамма\nПериод полураспада: 249.8 суток\nОпасный продукт коррозии и активации первого контура реакторов.");

        addItem("cd109",
                "Cadmium-109 (109Cd)\nDecay: Electron Capture\nHalf-life: 462.6 days\nSource for X-ray fluorescence analysis of metal alloys.",
                "Кадмий-109 (109Cd)\nРаспад: Электронный захват\nПериод полураспада: 462.6 суток\nИсточник рентгена для спектрального анализа сплавов металлов.");

        addItem("cd113m",
                "Cadmium-113m (113mCd)\nDecay: Beta- (580 keV)\nHalf-life: 14.1 years\nIsomeric activation product in nuclear reactor control blades.",
                "Кадмий-113m (113mCd)\nРаспад: Бета- (580 кэВ)\nПериод полураспада: 14.1 лет\nИзомерный продукт активации кадмиевых регулирующих стержней.");

        addItem("in111",
                "Indium-111 (111In)\nDecay: Electron Capture, Gamma (171 & 245 keV)\nHalf-life: 2.80 days\nUsed to track white blood cells and infection sites.",
                "Индий-111 (111In)\nРаспад: Электронный захват, Гамма (171 и 245 кэВ)\nПериод полураспада: 2.80 суток\nПрименяется для мечения лейкоцитов и поиска очагов инфекции.");

        addItem("sn119m",
                "Tin-119m (119mSn)\nDecay: Isomeric Transition, Gamma (23.9 keV)\nHalf-life: 293.1 days\nStandard gamma source for Mössbauer spectroscopy.",
                "Олово-119m (119mSn)\nРаспад: Изомерный переход, Гамма (23.9 кэВ)\nПериод полураспада: 293.1 суток\nЭталонный источник для мёссбауэровской спектроскопии олова.");

        addItem("sn126",
                "Tin-126 (126Sn)\nDecay: Beta- (305 keV)\nHalf-life: 230,000 years\nLong-lived hazardous nuclear waste fission product.",
                "Олово-126 (126Sn)\nРаспад: Бета- (305 кэВ)\nПериод полураспада: 230 тыс. лет\nВысокотоксичный долгоживущий продукт деления ядерного горючего.");

        addItem("sb124",
                "Antimony-124 (124Sb)\nDecay: Beta-, Intense Gamma (1.69 MeV)\nHalf-life: 60.2 days\nCoupled with Beryllium for Sb-Be startup neutron sources.",
                "Сурьма-124 (124Sb)\nРаспад: Бета-, Жесткая Гамма (1.69 МэВ)\nПериод полураспада: 60.2 суток\nИспользуется в Sb-Be источниках для физического пуска реакторов.");

        addItem("sb125",
                "Antimony-125 (125Sb)\nDecay: Beta-, Gamma\nHalf-life: 2.76 years\nFission product used to determine nuclear explosion yield.",
                "Сурьма-125 (125Sb)\nРаспад: Бета-, Гамма\nПериод полураспада: 2.76 года\nОсколок деления, маркер анализа мощности ядерных взрывов.");

        addItem("te129m",
                "Tellurium-129m (129mTe)\nDecay: Isomeric Transition, Beta-\nHalf-life: 33.6 days\nPrecursor to radiotoxic radioactive iodine.",
                "Теллур-129m (129mTe)\nРаспад: Изомерный переход, Бета-\nПериод полураспада: 33.6 суток\nПродукт деления, распадающийся в радиоактивный йод.");

        addItem("te132",
                "Tellurium-132 (132Te)\nDecay: Beta- (214 keV)\nHalf-life: 3.20 days\nDecays directly into hazardous Iodine-132.",
                "Теллур-132 (132Te)\nРаспад: Бета- (214 кэВ)\nПериод полураспада: 3.20 суток\nКороткоживущий предшественник опасного Йода-132 при авариях.");

        addItem("i123",
                "Iodine-123 (123I)\nDecay: Electron Capture, Gamma (159 keV)\nHalf-life: 13.22 hours\nOptimal clinical SPECT diagnostic agent for thyroid disorders.",
                "Йод-123 (123I)\nРаспад: Электронный захват, Гамма (159 кэВ)\nПериод полураспада: 13.22 часов\nИдеальный ОФЭКТ-диагностикум патологий щитовидной железы без лишней дозы.");

        addItem("i125",
                "Iodine-125 (125I)\nDecay: Electron Capture, Gamma (35 keV)\nHalf-life: 59.4 days\nWidely used in medical brachytherapy seeds.",
                "Йод-125 (125I)\nРаспад: Электронный захват, Гамма (35 кэВ)\nПериод полураспада: 59.4 суток\nШироко применяется в микрокапсулах для брахитерапии опухолей.");

        addItem("i129",
                "Iodine-129 (129I)\nDecay: Beta- (194 keV)\nHalf-life: 15.7 million years\nUltra long-lived mobile radioactive halogen waste.",
                "Йод-129 (129I)\nРаспад: Бета- (194 кэВ)\nПериод полураспада: 15.7 млн лет\nСверхдолгоживущий миграционно-подвижный ядерный отход.");

        addItem("i131",
                "Iodine-131 (131I)\nDecay: Beta- (606 keV), Gamma (364 keV)\nHalf-life: 8.02 days\nMajor radiological threat during fallout; used to treat thyroid cancer.",
                "Йод-131 (131I)\nРаспад: Бета- (606 кэВ), Гамма (364 кэВ)\nПериод полураспада: 8.02 суток\nГлавная опасность при авариях на АЭС; лекарство от рака щитовидной железы.");

        addItem("xe133",
                "Xenon-133 (133Xe)\nDecay: Beta-, Gamma (81 keV)\nHalf-life: 5.24 days\nNoble gas used to assess pulmonary ventilation function.",
                "Ксенон-133 (133Xe)\nРаспад: Бета-, Гамма (81 кэВ)\nПериод полураспада: 5.24 суток\nИнертный радиоактивный газ для оценки вентиляции лёгких.");

        addItem("xe135",
                "Xenon-135 (135Xe)\nDecay: Beta-\nHalf-life: 9.14 hours\nWorst reactor poison with a gigantic neutron capture cross-section (2.6M barns).",
                "Ксенон-135 (135Xe)\nРаспад: Бета-\nПериод полураспада: 9.14 часов\nСильнейший реакторный яд (захват 2.6 млн барн), виновник йодной ямы.");

        addItem("cs134",
                "Caesium-134 (134Cs)\nDecay: Beta-, Powerful Gamma cascade\nHalf-life: 2.06 years\nNeutron activation product used to verify fuel burnup.",
                "Цезий-134 (134Cs)\nРаспад: Бета-, Каскадная жесткая Гамма\nПериод полураспада: 2.06 года\nИндикатор выгорания топлива и маркер свежих радиоактивных выбросов.");

        addItem("cs135",
                "Caesium-135 (135Cs)\nDecay: Low-energy Beta- (269 keV)\nHalf-life: 2.3 million years\nLong-lived mobile fission product requiring geological isolation.",
                "Цезий-135 (135Cs)\nРаспад: Мягкий Бета- (269 кэВ)\nПериод полураспада: 2.3 млн лет\nСверхдолгоживущий отход деления ядерного горючего.");

        addItem("cs137",
                "Caesium-137 (137Cs)\nDecay: Beta- -> Ba-137m (662 keV Gamma)\nHalf-life: 30.17 years\nPrimary gamma-hazard in exclusion zones and irradiation plants.",
                "Цезий-137 (137Cs)\nРаспад: Бета- -> Ba-137m (Гамма 662 кэВ)\nПериод полураспада: 30.17 лет\nОсновной фактор радиационного заражения территорий и гамма-стерилизации.");

        addItem("ba133",
                "Barium-133 (133Ba)\nDecay: Electron Capture, Gamma (356 keV)\nHalf-life: 10.51 years\nStandard reference source for gamma-spectrometer calibration.",
                "Барий-133 (133Ba)\nРаспад: Электронный захват, Гамма (356 кэВ)\nПериод полураспада: 10.51 лет\nМноголинейный эталонный калибратор сцинтилляционных спектрометров.");

        addItem("ba140",
                "Barium-140 (140Ba)\nDecay: Beta- -> La-140\nHalf-life: 12.75 days\nProminent fission product that decays into high-energy Lanthanum.",
                "Барий-140 (140Ba)\nРаспад: Бета- -> La-140\nПериод полураспада: 12.75 суток\nОсколок деления, распадающийся в жесткий гамма-излучатель Лантан-140.");

        addItem("la140",
                "Lanthanum-140 (140La)\nDecay: Beta-, Intense Gamma (1.60 MeV)\nHalf-life: 40.3 hours\nShort-lived daughter of Barium-140 with penetrating radiation.",
                "Лантан-140 (140La)\nРаспад: Бета-, Высокоэнергетическая Гамма (1.60 МэВ)\nПериод полураспада: 40.3 часов\nДочерний нуклид Ba-140 с высокой проникающей способностью.");

        // =========================================================================
        // 4. ЛАНТАНОИДЫ И ПОГЛОТИТЕЛИ
        // =========================================================================
        addItem("ce141",
                "Cerium-141 (141Ce)\nDecay: Beta-, Gamma\nHalf-life: 32.5 days\nLanthanide fission product used for sediment transport studies.",
                "Церий-141 (141Ce)\nРаспад: Бета-, Гамма\nПериод полураспада: 32.5 суток\nЛантаноидный продукт деления, индикатор переноса взвесей в реках.");

        addItem("ce144",
                "Cerium-144 (144Ce)\nDecay: Beta- -> Pr-144 (3 MeV)\nHalf-life: 284.9 days\nHigh heat-producing radioisotope investigated for thermal generators.",
                "Церий-144 (144Ce)\nРаспад: Бета- -> Pr-144 (3 МэВ)\nПериод полураспада: 284.9 суток\nВысокотепловыделяющий изотоп ОЯТ, источник для термогенераторов.");

        addItem("pr143",
                "Praseodymium-143 (143Pr)\nDecay: Pure Beta- (934 keV)\nHalf-life: 13.57 days\nPure beta-emitting lanthanide fission product.",
                "Празеодим-143 (143Pr)\nРаспад: Чистый Бета- (934 кэВ)\nПериод полураспада: 13.57 суток\nЧистый бета-излучатель из группы лантаноидных продуктов деления.");

        addItem("nd147",
                "Neodymium-147 (147Nd)\nDecay: Beta-, Gamma\nHalf-life: 10.98 days\nPrecursor of Promethium-147 in nuclear reactors.",
                "Неодим-147 (147Nd)\nРаспад: Бета-, Гамма\nПериод полураспада: 10.98 суток\nПредшественник синтеза радиолюминофорного Прометия-147.");

        addItem("pm147",
                "Promethium-147 (147Pm)\nDecay: Pure Beta- (224 keV)\nHalf-life: 2.62 years\nUsed in self-luminous dials, thickness gauges, and atomic batteries.",
                "Прометий-147 (147Pm)\nРаспад: Чистый Бета- (224 кэВ)\nПериод полураспада: 2.62 года\nПрименяется в светосоставах постоянного действия и атомных батарейках.");

        addItem("sm149",
                "Samarium-149 (149Sm)\nDecay: Stable\nHalf-life: Infinite\nMajor non-decaying nuclear reactor poison (capture: 41,000 barns).",
                "Самарий-149 (149Sm)\nРаспад: Стабилен\nПериод полураспада: Бесконечен\nНерадиоактивный, но мощный неустранимый реакторный шлак.");

        addItem("sm151",
                "Samarium-151 (151Sm)\nDecay: Low-energy Beta- (76 keV)\nHalf-life: 90 years\nMedium-lived neutron poison in spent nuclear fuel.",
                "Самарий-151 (151Sm)\nРаспад: Мягкий Бета- (76 кэВ)\nПериод полураспада: 90 лет\nДолгоживущий реакторный яд, медленно выгорающий в нейтронном потоке.");

        addItem("sm153",
                "Samarium-153 (153Sm)\nDecay: Beta-, Gamma\nHalf-life: 46.5 hours\nTargeted therapeutic agent for severe bone metastases.",
                "Самарий-153 (153Sm)\nРаспад: Бета-, Гамма\nПериод полураспада: 46.5 часов\nТерапевтический радиопрепарат для купирования костных метастазов.");

        addItem("eu152",
                "Europium-152 (152Eu)\nDecay: Electron Capture (72%) / Beta- (28%)\nHalf-life: 13.54 years\nFormed by neutron activation of concrete and reactor control rods.",
                "Европий-152 (152Eu)\nРаспад: ЭЗ (72%) / Бета- (28%), Гамма\nПериод полураспада: 13.54 лет\nОбразуется при активации нейтронами бетона и поглощающих стержней.");

        addItem("eu154",
                "Europium-154 (154Eu)\nDecay: Beta-, High-energy Gamma\nHalf-life: 8.60 years\nLong-term gamma emitter in decommissioning reactor vessels.",
                "Европий-154 (154Eu)\nРаспад: Бета-, Жесткая Гамма\nПериод полураспада: 8.60 лет\nДолгоживущий гамма-излучатель конструкций выводимых из строя реакторов.");

        addItem("eu155",
                "Europium-155 (155Eu)\nDecay: Beta-, Soft Gamma (86 keV)\nHalf-life: 4.76 years\nLow-energy gamma source for industrial thickness measuring.",
                "Европий-155 (155Eu)\nРаспад: Бета-, Мягкая Гамма (86 кэВ)\nПериод полураспада: 4.76 лет\nИсточник низкоэнергетического излучения для бесконтактных толщиномеров.");

        addItem("gd153",
                "Gadolinium-153 (153Gd)\nDecay: Electron Capture, Gamma (97 & 103 keV)\nHalf-life: 240.4 days\nCalibration standard for dual-energy X-ray bone densitometers.",
                "Гадолиний-153 (153Gd)\nРаспад: Электронный захват, Гамма (97 и 103 кэВ)\nПериод полураспада: 240.4 суток\nКалибровочный источник для денситометрии плотности костей.");

        addItem("gd157",
                "Gadolinium-157 (157Gd)\nStable ultimate neutron absorber.\nWorld record thermal neutron capture cross section (254,000 barns).",
                "Гадолиний-157 (157Gd)\nСтабильный абсолютный поглотитель нейтронов.\nРекордное сечение захвата тепловых нейтронов (254,000 барн).");

        addItem("tb160",
                "Terbium-160 (160Tb)\nDecay: Beta-, Complex Gamma\nHalf-life: 72.3 days\nUsed as an industrial radiotracer in high-temperature metallurgy.",
                "Тербий-160 (160Tb)\nРаспад: Бета-, Сложный спектр Гамма\nПериод полураспада: 72.3 суток\nРадиоактивный трассер для высокотемпературной металлургии.");

        addItem("ho166m",
                "Holmium-166m (166mHo)\nDecay: Beta-, High-energy Gamma\nHalf-life: 1,200 years\nLongest-lived nuclear isomer of holmium.",
                "Гольмий-166m (166mHo)\nРаспад: Бета-, Жесткая Гамма\nПериод полураспада: 1,200 лет\nСверхдолгоживущий изомерный ядерный отход.");

        addItem("lu177",
                "Lutetium-177 (177Lu)\nDecay: Beta- (498 keV), Gamma (208 keV)\nHalf-life: 6.65 days\nBreakthrough radioisotope for targeted cancer peptide therapy.",
                "Лютеций-177 (177Lu)\nРаспад: Бета- (498 кэВ), Гамма (208 кэВ)\nПериод полураспада: 6.65 дней\nПередовой изотоп для таргетной пептидной терапии рака.");

        // =========================================================================
        // 5. ТЯЖЁЛЫЕ МЕТАЛЛЫ И ТЕРАПЕВТИЧЕСКИЕ ИЗОТОПЫ
        // =========================================================================
        addItem("hf178m2",
                "Hafnium-178m2 (178m2Hf)\nDecay: Isomeric Transition, Gamma cascade (2.45 MeV)\nHalf-life: 31 years\nHigh energy-density nuclear isomer investigated for gamma-ray lasers.",
                "Гафний-178m2 (178m2Hf)\nРаспад: Изомерный переход, Гамма (2.45 МэВ)\nПериод полураспада: 31 год\nЭнергоёмкий изомер; исследуется для создания гамма-лазеров.");

        addItem("ta182",
                "Tantalum-182 (182Ta)\nDecay: Beta-, High-energy Gamma (1.1 - 1.2 MeV)\nHalf-life: 114.4 days\nUsed for radiographic weld examination in thick metal structures.",
                "Тантал-182 (182Ta)\nРаспад: Бета-, Жесткая Гамма (1.1 - 1.2 МэВ)\nПериод полураспада: 114.4 суток\nГамма-дефектоскопия сварных швов массивных броневых и стальных конструкций.");

        addItem("w188",
                "Tungsten-188 (188W)\nDecay: Beta-\nHalf-life: 69.8 days\nParent radionuclide for generators of therapeutic Rhenium-188.",
                "Вольфрам-188 (188W)\nРаспад: Бета-\nПериод полураспада: 69.8 суток\nМатеринский нуклид для генераторов терапевтического Рения-188.");

        addItem("re186",
                "Rhenium-186 (186Re)\nDecay: Beta- (1.07 MeV), EC, Gamma\nHalf-life: 3.72 days\nTherapeutic radionuclide for radiosynovectomy of inflamed joints.",
                "Рений-186 (186Re)\nРаспад: Бета- (1.07 МэВ), ЭЗ, Гамма\nПериод полураспада: 3.72 суток\nПрименяется для радиосиновэктомии при тяжелых артритах суставов.");

        addItem("re188",
                "Rhenium-188 (188Re)\nDecay: High-energy Beta- (2.12 MeV), Gamma\nHalf-life: 17.0 hours\nIdeal radioisotope for transdermal skin cancer therapy.",
                "Рений-188 (188Re)\nРаспад: Жесткий Бета- (2.12 МэВ), Гамма\nПериод полураспада: 17.0 часов\nПрименяется в радионуклидной терапии базальноклеточного рака кожи.");

        addItem("ir192",
                "Iridium-192 (192Ir)\nDecay: Beta-, Penetrating Gamma (300 - 600 keV)\nHalf-life: 73.83 days\nGlobal standard source for pipeline and aircraft non-destructive testing.",
                "Иридий-192 (192Ir)\nРаспад: Бета-, Проникающая Гамма (300 - 600 кэВ)\nПериод полураспада: 73.83 суток\nМировой стандарт для гамма-контроля авиаконструкций и газопроводов.");

        addItem("au197",
                "Gold-197 (197Au)\nOnly stable natural gold isotope.\nActivation target to produce therapeutic Gold-198.",
                "Золото-197 (197Au)\nЕдинственный природный стабильный изотоп золота.\nМишень для нейтронного синтеза радиоактивного Золота-198.");

        addItem("au198",
                "Gold-198 (198Au)\nDecay: Beta- (961 keV), Gamma (412 keV)\nHalf-life: 2.70 days\nColloidal solution for targeted tumor cavity radiation.",
                "Золото-198 (198Au)\nРаспад: Бета- (961 кэВ), Гамма (412 кэВ)\nПериод полураспада: 2.70 суток\nКоллоидное радиоактивное золото для облучения опухолевых полостей.");

        addItem("tl201",
                "Thallium-201 (201Tl)\nDecay: Electron Capture, X-rays (68 - 80 keV)\nHalf-life: 73.1 hours\nClinical tracer for myocardial perfusion stress imaging.",
                "Таллий-201 (201Tl)\nРаспад: Электронный захват, Рентген (68 - 80 кэВ)\nПериод полураспада: 73.1 часов\nКлинический маркер перфузии миокарда при инфарктах и ишемии.");

        addItem("tl204",
                "Thallium-204 (204Tl)\nDecay: Pure Beta- (763 keV)\nHalf-life: 3.78 years\nIndustrial source for precise beta-gauge film thickness measurement.",
                "Таллий-204 (204Tl)\nРаспад: Чистый Бета- (763 кэВ)\nПериод полураспада: 3.78 года\nИсточник в радиоизотопных толщиномерах тонких пленок и фольги.");

        addItem("pb208",
                "Lead-208 (208Pb)\nDoubly magic stable nucleus (82 protons, 126 neutrons).\nHeaviest stable nuclide known in the universe.",
                "Свинец-208 (208Pb)\nДважды магическое стабильное ядро (82 протона, 126 нейтронов).\nСамый тяжёлый абсолютно стабильный нуклид во Вселенной.");

        // =========================================================================
        // 6. ЕСТЕСТВЕННЫЕ РЯДЫ И ТЯЖЁЛЫЕ АЛЬФА-ИЗЛУЧАТЕЛИ
        // =========================================================================
        addItem("pb210",
                "Lead-210 (210Pb)\nDecay: Beta- (63 keV)\nHalf-life: 22.20 years\nLong-lived breakdown product of Radon-222 in the Uranium series.",
                "Свинец-210 (210Pb)\nРаспад: Бета- (63 кэВ)\nПериод полураспада: 22.20 лет\nДолгоживущий продукт распада Радона-222 в ряду Урана-238.");

        addItem("pb212",
                "Lead-212 (212Pb)\nDecay: Beta- (574 keV)\nHalf-life: 10.64 hours\nGenerator parent for in-vivo alpha emitter Bismuth-212.",
                "Свинец-212 (212Pb)\nРаспад: Бета- (574 кэВ)\nПериод полураспада: 10.64 часов\nГенератор короткоживущего альфа-излучателя Висмута-212.");

        addItem("bi209",
                "Bismuth-209 (209Bi)\nDecay: Alpha (3.14 MeV)\nHalf-life: 2.01 x 10^19 years\nExtremely weak radioactivity; was considered the heaviest stable isotope until 2003.",
                "Висмут-209 (209Bi)\nРаспад: Сверхслабая Альфа (3.14 МэВ)\nПериод полураспада: 20.1 квинтиллионов лет\nДо 2003 года ошибочно считался стабильным элементом.");

        addItem("bi210",
                "Bismuth-210 (210Bi)\nDecay: Beta- (1.16 MeV)\nHalf-life: 5.01 days\nHistorically called Radium E; decays directly into Polonium-210.",
                "Висмут-210 (210Bi)\nРаспад: Бета- (1.16 МэВ)\nПериод полураспада: 5.01 суток\nИсторический Радий-E, мгновенно распадается в токсичный Полоний-210.");

        addItem("bi213",
                "Bismuth-213 (213Bi)\nDecay: Alpha (5.87 MeV) / Beta-\nHalf-life: 45.6 minutes\nHigh linear-energy alpha emitter for leukemia targeted therapy.",
                "Висмут-213 (213Bi)\nРаспад: Альфа (5.87 МэВ) / Бета-\nПериод полураспада: 45.6 минут\nКороткоживущий альфа-излучатель для селективной терапии лейкемии.");

        addItem("po209",
                "Polonium-209 (209Po)\nDecay: Alpha (4.88 MeV)\nHalf-life: 125.2 years\nLongest-lived polonium isotope; extremely dangerous alpha-radiotoxin.",
                "Полоний-209 (209Po)\nРаспад: Альфа (4.88 МэВ)\nПериод полураспада: 125.2 лет\nСамый долгоживущий изотоп полония, опаснейший альфа-токсин.");

        addItem("po210",
                "Polonium-210 (210Po)\nDecay: Pure Alpha (5.30 MeV)\nHalf-life: 138.4 days\nExtreme radiotoxicity and intense heat generation (140 W/g).",
                "Полоний-210 (210Po)\nРаспад: Чистая Альфа (5.30 МэВ)\nПериод полураспада: 138.4 суток\nСверхтоксичный альфа-излучатель с огромным тепловыделением (140 Вт/г).");

        addItem("at211",
                "Astatine-211 (211At)\nDecay: Alpha (42%) / EC (58%)\nHalf-life: 7.21 hours\nPremier heavy halogen for targeted alpha therapy (TAT) of glioblastoma.",
                "Астат-211 (211At)\nРаспад: Альфа (42%) / ЭЗ (58%)\nПериод полураспада: 7.21 часов\nТяжёлый галоген для таргетной альфа-терапии рака мозга.");

        addItem("rn219",
                "Radon-219 / Actinon (219Rn)\nDecay: Alpha (6.82 MeV)\nHalf-life: 3.96 seconds\nShort-lived radioactive noble gas of the Actinium series.",
                "Радон-219 / Актинон (219Rn)\nРаспад: Альфа (6.82 МэВ)\nПериод полураспада: 3.96 секунды\nКороткоживущий радиоактивный инертный газ ряда Урана-235.");

        addItem("rn220",
                "Radon-220 / Thoron (220Rn)\nDecay: Alpha (6.29 MeV)\nHalf-life: 55.6 seconds\nRadioactive noble gas from the Thorium-232 decay chain.",
                "Радон-220 / Торон (220Rn)\nРаспад: Альфа (6.29 МэВ)\nПериод полураспада: 55.6 секунд\nРадиоактивный инертный газ естественного ряда распада Тория-232.");

        addItem("rn222",
                "Radon-222 (222Rn)\nDecay: Alpha (5.49 MeV)\nHalf-life: 3.82 days\nMajor source of natural indoor radiation; daughter of Radium-226.",
                "Радон-222 (222Rn)\nРаспад: Альфа (5.49 МэВ)\nПериод полураспада: 3.82 суток\nТяжелый радиоактивный газ, скапливающийся в шахтах и подвалах.");

        addItem("fr223",
                "Francium-223 (223Fr)\nDecay: Beta- (99%) / Alpha (1%)\nHalf-life: 22.0 minutes\nMost stable isotope of francium; elusive natural alkali element.",
                "Франций-223 (223Fr)\nРаспад: Бета- (99%) / Альфа (1%)\nПериод полураспада: 22.0 минуты\nСамый долгоживущий изотоп исчезающе редкого франция.");

        addItem("ra223",
                "Radium-223 (223Ra)\nDecay: Alpha cascade (28 MeV total)\nHalf-life: 11.43 days\nBone-seeking alpha-emitting pharmaceutical (Xofigo).",
                "Радий-223 (223Ra)\nРаспад: Каскадная Альфа (сумма 28 МэВ)\nПериод полураспада: 11.43 суток\nПрепарат Ксофиго для альфа-облучения костных очагов.");

        addItem("ra224",
                "Radium-224 (224Ra)\nDecay: Alpha (5.69 MeV)\nHalf-life: 3.63 days\nHistorically known as Thorium X in radiobiology.",
                "Радий-224 (224Ra)\nРаспад: Альфа (5.69 МэВ)\nПериод полураспада: 3.63 суток\nИсторический Торий-X из ториевого ряда радиоактивного распада.");

        addItem("ra226",
                "Radium-226 (226Ra)\nDecay: Alpha (4.78 MeV), Gamma\nHalf-life: 1,600 years\nThe legendary element isolated by Marie & Pierre Curie.",
                "Радий-226 (226Ra)\nРаспад: Альфа (4.78 МэВ), Гамма\nПериод полураспада: 1,600 лет\nКлассический радий, открытый супругами Кюри. Источник радона.");

        addItem("ra228",
                "Radium-228 (228Ra)\nDecay: Beta- (46 keV)\nHalf-life: 5.75 years\nHistorically named Mesothorium 1; used in old luminous paints.",
                "Радий-228 (228Ra)\nРаспад: Бета- (46 кэВ)\nПериод полураспада: 5.75 лет\nИсторический Мезоторий-1, входил в состав светящихся красок циферблатов.");

        addItem("ac225",
                "Actinium-225 (225Ac)\nDecay: Alpha cascade (4 alpha particles)\nHalf-life: 9.92 days\nThe premier isotope for Targeted Alpha Therapy (TAT).",
                "Актиний-225 (225Ac)\nРаспад: Каскад 4-х Альфа-частиц\nПериод полураспада: 9.92 суток\nСамый востребованный нуклид современной таргетной альфа-терапии рака.");

        addItem("ac227",
                "Actinium-227 (227Ac)\nDecay: Beta- (98.6%) / Alpha (1.4%)\nHalf-life: 21.77 years\nUsed in compact laboratory Ac-Be neutron sources.",
                "Актиний-227 (227Ac)\nРаспад: Бета- (98.6%) / Альфа (1.4%)\nПериод полураспада: 21.77 лет\nПрименяется в мощных лабораторных нейтронных источниках Ac-Be.");

        // =========================================================================
        // 7. ТОРИЙ, ПРОТАКТИНИЙ И УРАН
        // =========================================================================
        addItem("th227",
                "Thorium-227 (227Th)\nDecay: Alpha (6.04 MeV)\nHalf-life: 18.70 days\nDaughter of Ac-227 used in alpha-immunoconjugates.",
                "Торий-227 (227Th)\nРаспад: Альфа (6.04 МэВ)\nПериод полураспада: 18.70 суток\nРадиоактиний, компонент альфа-иммуноконъюгатов.");

        addItem("th228",
                "Thorium-228 (228Th)\nDecay: Alpha (5.42 MeV)\nHalf-life: 1.91 years\nRadiothorium; rapid producer of energetic alpha emitters.",
                "Торий-228 (228Th)\nРаспад: Альфа (5.42 МэВ)\nПериод полураспада: 1.91 года\nРадиоторий, быстро накапливающий жесткий гамма-фон.");

        addItem("th229",
                "Thorium-229 (229Th)\nDecay: Alpha (4.84 MeV)\nHalf-life: 7,932 years\nPossesses the lowest nuclear isomer state (8.3 eV) for nuclear laser clocks.",
                "Торий-229 (229Th)\nРаспад: Альфа (4.84 МэВ)\nПериод полураспада: 7,932 лет\nИзомер с рекордно низким возбуждением (8.3 эВ) для оптических ядерных часов.");

        addItem("th230",
                "Thorium-230 (230Th)\nDecay: Alpha (4.69 MeV)\nHalf-life: 75,380 years\nIonium; used for Uranium-Thorium dating of ocean sediments.",
                "Торий-230 (230Th)\nРаспад: Альфа (4.69 МэВ)\nПериод полураспада: 75,380 лет\nИоний, применяется в палеоокеанографии для уран-ториевого датирования.");

        addItem("th231",
                "Thorium-231 (231Th)\nDecay: Beta- (390 keV), Gamma\nHalf-life: 25.5 hours\nDirect daughter product of Uranium-235 alpha decay.",
                "Торий-231 (231Th)\nРаспад: Бета- (390 кэВ), Гамма\nПериод полураспада: 25.5 часов\nПрямой продукт альфа-распада Урана-235.");

        addItem("th232",
                "Thorium-232 (232Th)\nDecay: Alpha (4.01 MeV)\nHalf-life: 14.05 billion years\nPrimary natural fertile isotope for the Thorium-Uranium nuclear fuel cycle.",
                "Торий-232 (232Th)\nРаспад: Альфа (4.01 МэВ)\nПериод полураспада: 14.05 млрд лет\nПриродный торий, сырьевой материал для ториевого ядерного цикла.");

        addItem("th234",
                "Thorium-234 (234Th)\nDecay: Beta- (272 keV)\nHalf-life: 24.10 days\nUranium X1; direct daughter product of Uranium-238 alpha decay.",
                "Торий-234 (234Th)\nРаспад: Бета- (272 кэВ)\nПериод полураспада: 24.10 суток\nУран-X1, первичный дочерний продукт распада природного Урана-238.");

        addItem("pa231",
                "Protactinium-231 (231Pa)\nDecay: Alpha (5.06 MeV)\nHalf-life: 32,760 years\nRare and toxic naturally occurring decay product of U-235.",
                "Протактиний-231 (231Pa)\nРаспад: Альфа (5.06 МэВ)\nПериод полураспада: 32,760 лет\nПриродный долгоживущий продукт ряда Урана-235.");

        addItem("pa233",
                "Protactinium-233 (233Pa)\nDecay: Beta- (570 keV), Gamma (312 keV)\nHalf-life: 26.97 days\nCrucial intermediate isotope during U-233 breeding in thorium reactors.",
                "Протактиний-233 (233Pa)\nРаспад: Бета- (570 кэВ), Гамма (312 кэВ)\nПериод полураспада: 26.97 суток\nПромежуточный изотоп при наработке Урана-233 в ториевых реакторах.");

        addItem("pa234m",
                "Protactinium-234m (234mPa)\nDecay: High-energy Beta- (2.28 MeV)\nHalf-life: 1.17 minutes\nUranium X2; responsible for the strong beta emission from uranium ore.",
                "Протактиний-234m (234mPa)\nРаспад: Жесткий Бета- (2.28 МэВ)\nПериод полураспада: 1.17 минут\nУран-X2, отвечает за мощное бета-излучение необработанного урана.");

        addItem("u230",
                "Uranium-230 (230U)\nDecay: Alpha (5.89 MeV)\nHalf-life: 20.8 days\nParent isotope in generators for targeted alpha therapy (TAT).",
                "Уран-230 (230U)\nРаспад: Альфа (5.89 МэВ)\nПериод полураспада: 20.8 суток\nПерспективный генераторный изотоп таргетной альфа-терапии рака.");

        addItem("u232",
                "Uranium-232 (232U)\nDecay: Alpha (5.32 MeV)\nHalf-life: 68.9 years\nProduces Th-228 and energetic Tl-208 gamma rays (proliferation barrier).",
                "Уран-232 (232U)\nРаспад: Альфа (5.32 МэВ)\nПериод полураспада: 68.9 лет\nНакапливает смертоносный Таллий-208; барьер против ядерного распространения.");

        addItem("u233",
                "Uranium-233 (233U)\nDecay: Alpha (4.82 MeV)\nHalf-life: 159,200 years\nClean and potent fissile isotope bred from Thorium-232.",
                "Уран-233 (233U)\nРаспад: Альфа (4.82 МэВ)\nПериод полураспада: 159,200 лет\nДелящийся ядерный материал, нарабатываемый из тория.");

        addItem("u234",
                "Uranium-234 (234U)\nDecay: Alpha (4.77 MeV)\nHalf-life: 245,500 years\nMinor natural uranium isotope; concentrates during enrichment.",
                "Уран-234 (234U)\nРаспад: Альфа (4.77 МэВ)\nПериод полураспада: 245,500 лет\nПриродный спутник U-238, концентрирующийся при газоцентрифужном обогащении.");

        addItem("u235",
                "Uranium-235 (235U)\nDecay: Alpha (4.40 MeV)\nHalf-life: 703.8 million years\nThe only naturally occurring fissile nuclide; fuels commercial reactors.",
                "Уран-235 (235U)\nРаспад: Альфа (4.40 МэВ)\nПериод полураспада: 703.8 млн лет\nЕдинственный природный делящийся изотоп; основа атомной энергетики.");

        addItem("u236",
                "Uranium-236 (236U)\nDecay: Alpha (4.49 MeV)\nHalf-life: 23.42 million years\nNeutron poison formed by parasitic non-fission capture in U-235.",
                "Уран-236 (236U)\nРаспад: Альфа (4.49 МэВ)\nПериод полураспада: 23.42 млн лет\nОбразуется паразитным захватом нейтронов в U-235; загрязнитель регенерированного урана.");

        addItem("u237",
                "Uranium-237 (237U)\nDecay: Beta- (519 keV), Gamma\nHalf-life: 6.75 days\nFormed via (n,2n) reactions in fast-neutron reactors.",
                "Уран-237 (237U)\nРаспад: Бета- (519 кэВ), Гамма\nПериод полураспада: 6.75 суток\nОбразуется в быстрых реакторах; распадается в Нептуний-237.");

        addItem("u238",
                "Uranium-238 (238U)\nDecay: Alpha (4.20 MeV)\nHalf-life: 4.468 billion years\nDominates natural uranium (99.27%); fertile precursor to Plutonium-239.",
                "Уран-238 (238U)\nРаспад: Альфа (4.20 МэВ)\nПериод полураспада: 4.468 млрд лет\nОсновная масса природного урана (99.27%); сырьё для наработки Плутония-239.");

        // =========================================================================
        // 8. ТРАНСУРАНЫ: НЕПТУНИЙ, ПЛУТОНИЙ, АМЕРИЦИЙ, КЮРИЙ
        // =========================================================================
        addItem("np237",
                "Neptunium-237 (237Np)\nDecay: Alpha (4.87 MeV)\nHalf-life: 2.14 million years\nReactor by-product irradiated with neutrons to synthesize Plutonium-238.",
                "Нептуний-237 (237Np)\nРаспад: Альфа (4.87 МэВ)\nПериод полураспада: 2.14 млн лет\nСырьё реакторного облучения для промышленного синтеза Плутония-238.");

        addItem("np238",
                "Neptunium-238 (238Np)\nDecay: Beta- (1.29 MeV)\nHalf-life: 2.11 days\nShort-lived intermediate in the production of RTG-grade Pu-238.",
                "Нептуний-238 (238Np)\nРаспад: Бета- (1.29 МэВ)\nПериод полураспада: 2.11 суток\nКороткоживущий предшественник чистого Плутония-238.");

        addItem("np239",
                "Neptunium-239 (239Np)\nDecay: Beta- (436 keV), Gamma\nHalf-life: 2.35 days\nEssential link connecting U-238 neutron capture to weapons-grade Pu-239.",
                "Нептуний-239 (239Np)\nРаспад: Бета- (436 кэВ), Гамма\nПериод полураспада: 2.35 суток\nКлючевое звено превращения Урана-238 в оружейный Плутоний-239.");

        addItem("pu236",
                "Plutonium-236 (236Pu)\nDecay: Alpha (5.77 MeV)\nHalf-life: 2.87 years\nUndesirable high-activity contaminant in recycled nuclear fuel.",
                "Плутоний-236 (236Pu)\nРаспад: Альфа (5.77 МэВ)\nПериод полураспада: 2.87 года\nВысокоактивная короткоживущая примесь в переработанном ядерном топливе.");

        addItem("pu238",
                "Plutonium-238 (238Pu)\nDecay: Pure Alpha (5.50 MeV)\nHalf-life: 87.7 years\nIdeal nuclear heat source (0.57 W/g) powering deep-space RTGs.",
                "Плутоний-238 (238Pu)\nРаспад: Чистая Альфа (5.50 МэВ)\nПериод полураспада: 87.7 лет\nТопливо для космических РИТЭГ (Вояджер, Персеверанс). Выделяет 0.57 Вт/г.");

        addItem("pu239",
                "Plutonium-239 (239Pu)\nDecay: Alpha (5.16 MeV)\nHalf-life: 24,110 years\nPrimary weapons-grade and reactor fissile material; critical mass ~10 kg.",
                "Плутоний-239 (239Pu)\nРаспад: Альфа (5.16 МэВ)\nПериод полураспада: 24,110 лет\nГлавный оружейный и энергетический делящийся актинид. Критическая масса ~10 кг.");

        addItem("pu240",
                "Plutonium-240 (240Pu)\nDecay: Alpha (5.17 MeV), High Spontaneous Fission\nHalf-life: 6,561 years\nEmits background neutrons; limits the weapon utility of reactor-grade plutonium.",
                "Плутоний-240 (240Pu)\nРаспад: Альфа (5.17 МэВ), Спонтанное деление\nПериод полураспада: 6,561 лет\nИсточник спонтанных нейтронов; снижает качество оружейного плутония.");

        addItem("pu241",
                "Plutonium-241 (241Pu)\nDecay: Beta- (20.8 keV)\nHalf-life: 14.33 years\nFissile isotope that decays into Americium-241.",
                "Плутоний-241 (241Pu)\nРаспад: Бета- (20.8 кэВ)\nПериод полураспада: 14.33 лет\nДелящийся изотоп плутония; при распаде образует Америций-241.");

        addItem("pu242",
                "Plutonium-242 (242Pu)\nDecay: Alpha (4.90 MeV)\nHalf-life: 373,000 years\nNon-fissile fertile isotope formed by sequential neutron captures.",
                "Плутоний-242 (242Pu)\nРаспад: Альфа (4.90 МэВ)\nПериод полураспада: 373 тыс. лет\nТяжёлый неделящийся изотоп, конечный продукт выгорания плутония.");

        addItem("pu244",
                "Plutonium-244 (244Pu)\nDecay: Alpha (4.59 MeV)\nHalf-life: 80.8 million years\nThe only primordial plutonium isotope detected in interstellar dust.",
                "Плутоний-244 (244Pu)\nРаспад: Альфа (4.59 МэВ)\nПериод полураспада: 80.8 млн лет\nСверхдолгоживущий реликтовый актинид, встречающийся в межзвёздной пыли.");

        addItem("am241",
                "Americium-241 (241Am)\nDecay: Alpha (5.48 MeV), Gamma (60 keV)\nHalf-life: 432.2 years\nUsed in household ionization smoke detectors and industrial gauges.",
                "Америций-241 (241Am)\nРаспад: Альфа (5.48 МэВ), Гамма (60 кэВ)\nПериод полураспада: 432.2 лет\nСердце ионизационных датчиков дыма и источник в гамма-толщиномерах.");

        addItem("am242m",
                "Americium-242m (242mAm)\nDecay: Isomeric Transition (82%) / Alpha / Fission\nHalf-life: 141 years\nBoasts the highest thermal neutron fission cross section (6,600 barns).",
                "Америций-242m (242mAm)\nРаспад: Изомерный переход (82%) / Деление\nПериод полураспада: 141 год\nРекордное сечение деления тепловыми нейтронами (6,600 барн). Топливо будущего.");

        addItem("am243",
                "Americium-243 (243Am)\nDecay: Alpha (5.28 MeV)\nHalf-life: 7,370 years\nLong-lived actinide target for synthesizing Curium and heavier elements.",
                "Америций-243 (243Am)\nРаспад: Альфа (5.28 МэВ)\nПериод полураспада: 7,370 лет\nДолгоживущая мишень в ядерных реакторах для наработки Кюрия и Калифорния.");

        addItem("cm242",
                "Curium-242 (242Cm)\nDecay: Alpha (6.11 MeV)\nHalf-life: 162.8 days\nExtreme specific thermal power (120 W/g) capable of glowing red hot.",
                "Кюрий-242 (242Cm)\nРаспад: Альфа (6.11 МэВ)\nПериод полураспада: 162.8 суток\nКолоссальная тепловая мощность (120 Вт/г), раскаляющая металл докрасна.");

        addItem("cm243",
                "Curium-243 (243Cm)\nDecay: Alpha (5.79 MeV), Fission\nHalf-life: 29.1 years\nFissile transuranic isotope with high radioactive toxicity.",
                "Кюрий-243 (243Cm)\nРаспад: Альфа (5.79 МэВ), Деление\nПериод полураспада: 29.1 лет\nДелящийся трансурановый изотоп с высокой радиотоксичностью.");

        addItem("cm244",
                "Curium-244 (244Cm)\nDecay: Alpha (5.80 MeV), Spontaneous Fission\nHalf-life: 18.11 years\nPrimary alpha source aboard extraterrestrial rovers (APXS sensors).",
                "Кюрий-244 (244Cm)\nРаспад: Альфа (5.80 МэВ), Спонтанное деление\nПериод полураспада: 18.11 лет\nАльфа-источник рентгеновских спектрометров APXS на марсоходах.");

        addItem("cm245",
                "Curium-245 (245Cm)\nDecay: Alpha (5.36 MeV), Fissile\nHalf-life: 8,500 years\nHighly fissile isotope with a low bare-sphere critical mass (~12 kg).",
                "Кюрий-245 (245Cm)\nРаспад: Альфа (5.36 МэВ), Деление\nПериод полураспада: 8,500 лет\nЭффективный делящийся актинид с малой критической массой (~12 кг).");

        addItem("cm246",
                "Curium-246 (246Cm)\nDecay: Alpha (5.39 MeV)\nHalf-life: 4,760 years\nAccumulates in multi-recycled actinide fuel cycles.",
                "Кюрий-246 (246Cm)\nРаспад: Альфа (5.39 МэВ)\nПериод полураспада: 4,760 лет\nНакапливается при многократном рециклировании ОЯТ в быстрых реакторах.");

        addItem("cm247",
                "Curium-247 (247Cm)\nDecay: Alpha (5.27 MeV)\nHalf-life: 15.6 million years\nUltra long-lived fissile curium isotope.",
                "Кюрий-247 (247Cm)\nРаспад: Альфа (5.27 МэВ)\nПериод полураспада: 15.6 млн лет\nСверхдолгоживущий делящийся изотоп кюрия.");

        addItem("cm248",
                "Curium-248 (248Cm)\nDecay: Alpha (8%) / Spontaneous Fission (92%)\nHalf-life: 348,000 years\nThe premier heavy target material for superheavy element synthesis.",
                "Кюрий-248 (248Cm)\nРаспад: Альфа (8%) / Спонтанное деление (92%)\nПериод полураспада: 348 тыс. лет\nГлавный мишенный материал циклотронов для синтеза сверхтяжелых элементов.");

        // =========================================================================
        // 9. БЕРКЛИЙ, КАЛИФОРНИЙ, ЭЙНШТЕЙНИЙ, ФЕРМИЙ
        // =========================================================================
        addItem("bk247",
                "Berkelium-247 (247Bk)\nDecay: Alpha (5.68 MeV)\nHalf-life: 1,380 years\nMost stable isotope of berkelium; difficult to produce in reactors.",
                "Берклий-247 (247Bk)\nРаспад: Альфа (5.68 МэВ)\nПериод полураспада: 1,380 лет\nСамый долгоживущий изотоп берклия, крайне сложный в синтезе.");

        addItem("bk249",
                "Berkelium-249 (249Bk)\nDecay: Beta- (125 keV) / Low Alpha\nHalf-life: 330 days\nUsed as the nuclear target to synthesize Tennessine (element 117).",
                "Берклий-249 (249Bk)\nРаспад: Бета- (125 кэВ) / Альфа\nПериод полураспада: 330 суток\nМишень для синтеза сверхтяжелого Теннессина (элемент 117).");

        addItem("cf249",
                "Californium-249 (249Cf)\nDecay: Alpha (5.81 MeV), Gamma\nHalf-life: 351 years\nDaughter product of Berkelium-249 decay.",
                "Калифорний-249 (249Cf)\nРаспад: Альфа (5.81 МэВ), Гамма\nПериод полураспада: 351 год\nОбразуется в результате бета-распада накопленного Берклия-249.");

        addItem("cf250",
                "Californium-250 (250Cf)\nDecay: Alpha (89%) / Spontaneous Fission (11%)\nHalf-life: 13.08 years\nIntense spontaneous neutron emitter.",
                "Калифорний-250 (250Cf)\nРаспад: Альфа (89%) / Спонтанное деление (11%)\nПериод полураспада: 13.08 лет\nМощный источник нейтронов спонтанного деления.");

        addItem("cf251",
                "Californium-251 (251Cf)\nDecay: Alpha (5.85 MeV)\nHalf-life: 900 years\nExtremely low critical mass (~5-9 kg); potent fissile material.",
                "Калифорний-251 (251Cf)\nРаспад: Альфа (5.85 МэВ)\nПериод полураспада: 900 лет\nРекордно низкая критическая масса (~5-9 кг). Сильнейший делящийся нуклид.");

        addItem("cf252",
                "Californium-252 (252Cf)\nDecay: Alpha (96.9%) / Spontaneous Fission (3.1%)\nHalf-life: 2.645 years\nEmits 2.3 trillion neutrons per second per gram; used for nuclear reactor startup.",
                "Калифорний-252 (252Cf)\nРаспад: Альфа (96.9%) / Спонтанное деление (3.1%)\nПериод полураспада: 2.645 лет\nВыделяет 2.3 триллиона нейтронов/сек на грамм. Пусковой источник для реакторов.");

        addItem("es253",
                "Einsteinium-253 (253Es)\nDecay: Alpha (6.63 MeV)\nHalf-life: 20.47 days\nHistorically used to synthesize Mendelevium (element 101).",
                "Эйнштейний-253 (253Es)\nРаспад: Альфа (6.63 МэВ)\nПериод полураспада: 20.47 суток\nИсторически использован для первого в мире синтеза Менделевия (101).");

        addItem("es254",
                "Einsteinium-254 (254Es)\nDecay: Alpha (6.43 MeV)\nHalf-life: 275.7 days\nHeavy actinide used to calibrate mass spectrometers.",
                "Эйнштейний-254 (254Es)\nРаспад: Альфа (6.43 МэВ)\nПериод полураспада: 275.7 суток\nСамый долгоживущий изотоп эйнштейна; мишень для синтеза сверхтяжелых ядер.");

        addItem("fm255",
                "Fermium-255 (255Fm)\nDecay: Alpha (7.02 MeV)\nHalf-life: 20.07 hours\nDiscovered in debris from the Ivy Mike thermonuclear test explosion.",
                "Фермий-255 (255Fm)\nРаспад: Альфа (7.02 МэВ)\nПериод полураспада: 20.07 часов\nВпервые обнаружен в радиоактивных осадках термоядерного взрыва Ivy Mike.");

        addItem("fm257",
                "Fermium-257 (257Fm)\nDecay: Alpha / Spontaneous Fission\nHalf-life: 100.5 days\nThe practical limit of mass reachable by sequential neutron capture in reactors.",
                "Фермий-257 (257Fm)\nРаспад: Альфа / Спонтанное деление\nПериод полураспада: 100.5 суток\nФизический предел синтеза трансуранов методом нейтронного захвата в реакторах.");

        // =========================================================================
        // 10. ТРАНСФЕРМИЕВЫЕ И СВЕРХТЯЖЁЛЫЕ ЭЛЕМЕНТЫ (ЭЛЕМЕНТЫ 101 - 118)
        // =========================================================================
        addItem("md258",
                "Mendelevium-258 (258Md)\nDecay: Alpha / Spontaneous Fission\nHalf-life: 51.5 days\nLongest-lived isotope of mendelevium.",
                "Менделевий-258 (258Md)\nРаспад: Альфа / Спонтанное деление\nПериод полураспада: 51.5 суток\nСамый долгоживущий изотоп элемента 101.");

        addItem("no259",
                "Nobelium-259 (259No)\nDecay: Alpha (62%) / EC (23%) / Fission\nHalf-life: 58 minutes\nHeavy actinide synthesized via accelerator fusion.",
                "Нобелий-259 (259No)\nРаспад: Альфа (62%) / ЭЗ (23%) / Деление\nПериод полураспада: 58 минут\nТрансурановый изотоп, нарабатываемый на ускорителях тяжёлых ионов.");

        addItem("lr262",
                "Lawrencium-262 (262Lr)\nDecay: Electron Capture / Fission / Alpha\nHalf-life: 3.6 hours\nThe final member of the actinide series.",
                "Лоуренсий-262 (262Lr)\nРаспад: Электронный захват / Деление / Альфа\nПериод полураспада: 3.6 часа\nЗавершающий элемент ряда актинидов.");

        addItem("rf267",
                "Rutherfordium-267 (267Rf)\nDecay: Spontaneous Fission (80%) / Alpha\nHalf-life: 1.3 hours\nFirst transactinide superheavy element (Z=104).",
                "Резерфордий-267 (267Rf)\nРаспад: Спонтанное деление (80%) / Альфа\nПериод полураспада: 1.3 часа\nПервый сверхтяжелый элемент 4-й группы (Z=104).");

        addItem("db268",
                "Dubnium-268 (268Db)\nDecay: Spontaneous Fission\nHalf-life: 16 hours\nNamed in honor of JINR in Dubna, Russia.",
                "Дубний-268 (268Db)\nРаспад: Спонтанное деление\nПериод полураспада: 16 часов\nНазван в честь Объединённого института ядерных исследований в Дубне.");

        addItem("sg271",
                "Seaborgium-271 (271Sg)\nDecay: Alpha (67%) / Fission (33%)\nHalf-life: 2.4 minutes\nNamed after Nobel laureate Glenn Seaborg.",
                "Сиборгий-271 (271Sg)\nРаспад: Альфа (67%) / Деление (33%)\nПериод полураспада: 2.4 минуты\nСверхтяжелый элемент группы 6 (Z=106).");

        addItem("bh270",
                "Bohrium-270 (270Bh)\nDecay: Alpha (8.93 MeV)\nHalf-life: 61 seconds\nNamed in honor of physicist Niels Bohr.",
                "Борий-270 (270Bh)\nРаспад: Альфа (8.93 МэВ)\nПериод полураспада: 61 секунда\nСверхтяжелый элемент группы 7 (Z=107).");

        addItem("hs277",
                "Hassium-277 (277Hs)\nDecay: Spontaneous Fission\nHalf-life: 30 seconds\nForms volatile tetroxide similar to Osmium.",
                "Хассий-277 (277Hs)\nРаспад: Спонтанное деление\nПериод полураспада: 30 секунд\nХимический аналог осмия, образует летучий тетроксид.");

        addItem("mt278",
                "Meitnerium-278 (278Mt)\nDecay: Alpha (9.50 MeV)\nHalf-life: 4.5 seconds\nNamed after pioneering nuclear physicist Lise Meitner.",
                "Мейтнерий-278 (278Mt)\nРаспад: Альфа (9.50 МэВ)\nПериод полураспада: 4.5 секунды\nНазван в честь физика Лизы Мейтнер.");

        addItem("ds281",
                "Darmstadtium-281 (281Ds)\nDecay: Spontaneous Fission (90%) / Alpha\nHalf-life: 12.7 seconds\nSuperheavy element 110.",
                "Дармштадтий-281 (281Ds)\nРаспад: Спонтанное деление (90%) / Альфа\nПериод полураспада: 12.7 секунд\nСверхтяжелый элемент группы 10 (Z=110).");

        addItem("rg282",
                "Roentgenium-282 (282Rg)\nDecay: Alpha (9.00 MeV)\nHalf-life: 2.1 seconds\nNamed after Wilhelm Röntgen, discoverer of X-rays.",
                "Рентгений-282 (282Rg)\nРаспад: Альфа (9.00 МэВ)\nПериод полураспада: 2.1 секунды\nСверхтяжелый аналог золота (Z=111).");

        addItem("cn285",
                "Copernicium-285 (285Cn)\nDecay: Alpha (9.15 MeV)\nHalf-life: 29 seconds\nVolatile superheavy metal exhibiting noble-gas like traits.",
                "Коперниций-285 (285Cn)\nРаспад: Альфа (9.15 МэВ)\nПериод полураспада: 29 секунд\nЛетучий сверхтяжелый металл 12 группы (Z=112).");

        addItem("nh286",
                "Nihonium-286 (286Nh)\nDecay: Alpha (9.63 MeV)\nHalf-life: 9.5 seconds\nFirst superheavy element discovered in Asia (RIKEN, Japan).",
                "Нихоний-286 (286Nh)\nРаспад: Альфа (9.63 МэВ)\nПериод полураспада: 9.5 секунд\nПервый сверхтяжелый элемент, открытый в Азии (RIKEN, Япония).");

        addItem("fl289",
                "Flerovium-289 (289Fl)\nDecay: Alpha (9.82 MeV)\nHalf-life: 1.9 seconds\nLocated at the center of the theoretical Island of Nuclear Stability.",
                "Флёровий-289 (289Fl)\nРаспад: Альфа (9.82 МэВ)\nПериод полураспада: 1.9 секунды\nРасположен вблизи центра теоретического Острова стабильности (Z=114).");

        addItem("mc290",
                "Moscovium-290 (290Mc)\nDecay: Alpha (10.0 MeV)\nHalf-life: 0.65 seconds\nSynthesized at the Joint Institute for Nuclear Research, Dubna.",
                "Московий-290 (290Mc)\nРаспад: Альфа (10.0 МэВ)\nПериод полураспада: 0.65 секунды\nСверхтяжелый элемент 15 группы (Z=115).");

        addItem("lv293",
                "Livermorium-293 (293Lv)\nDecay: Alpha (10.54 MeV)\nHalf-life: 53 milliseconds\nSynthesized via Curium-248 and Calcium-48 fusion.",
                "Ливерморий-293 (293Lv)\nРаспад: Альфа (10.54 МэВ)\nПериод полураспада: 53 миллисекунды\nПродукт слияния ядер Кюрия-248 и Кальция-48 (Z=116).");

        addItem("ts294",
                "Tennessine-294 (294Ts)\nDecay: Alpha (10.81 MeV)\nHalf-life: 51 milliseconds\nSuperheavy halogen synthesized using Berkelium-249 targets.",
                "Теннессин-294 (294Ts)\nРаспад: Альфа (10.81 МэВ)\nПериод полураспада: 51 миллисекунда\nСверхтяжелый аналог астата, синтезированный на мишени Берклия-249 (Z=117).");

        addItem("og294",
                "Oganesson-294 (294Og)\nDecay: Alpha (11.65 MeV)\nHalf-life: 0.69 milliseconds\nThe heaviest element known to science (Z=118), named after Yuri Oganessian.",
                "Оганесон-294 (294Og)\nРаспад: Альфа (11.65 МэВ)\nПериод полураспада: 0.69 миллисекунды\nСамый тяжелый элемент таблицы Менделеева (Z=118).");

        // =========================================================================
        // 11. ПРИРОДНЫЕ РУДЫ И ПЕРВИЧНЫЕ МИНЕРАЛЫ (ORES)
        // =========================================================================
        addItem("ore_uraninite",
                "Uraninite Ore\nFormula: UO2 (with U3O8, Pb, Ra)\nUranium content: 88.15% U (Pure UO2) / 50-85% in ore\nPrimary source of natural uranium. Forms dense pitch-black crystals.",
                "Уранинит (Смоляная руда)\nФормула: UO2 (с примесью U3O8, Pb, Ra)\nСодержание урана: 88.15% U (в стехиометрии UO2) / 50-85% в руде\nГлавный первичный минерал урана. Тяжёлые смоляно-чёрные кубические кристаллы.");

        addItem("ore_pitchblende",
                "Pitchblende\nFormula: U3O8 (colloid uraninite variety)\nUranium content: ~84.80% U\nMassive cryptocrystalline uranium ore. Extremely high radon exhalation rate.",
                "Настуран (Урановая смолка)\nФормула: U3O8 (колломорфный агрегат)\nСодержание урана: ~84.80% U\nМассивная почковидная смоляная руда с интенсивным выделением радона-222.");

        addItem("ore_coffinite",
                "Coffinite\nFormula: U(SiO4)1-x(OH)4x\nUranium content: 60.0 - 72.6% U\nMajor silicate ore mineral in sandstone-hosted roll-front deposits.",
                "Коффинит\nФормула: U(SiO4)1-x(OH)4x\nСодержание урана: 60.0 - 72.6% U\nВажнейший силикатный урановый минерал осадочных пластовых месторождений.");

        addItem("ore_brannerite",
                "Brannerite\nFormula: UTi2O6\nUranium content: 62.8% U, 21.1% Ti\nRefractory black radioactive titanate ore found in quartz pebble conglomerates.",
                "Браннерит\nФормула: UTi2O6\nСодержание урана: 62.8% U, 21.1% Ti\nУпорный титанат урана; главная руда месторождений типа Эллиот-Лейк.");

        addItem("ore_davidite",
                "Davidite-(La)\nFormula: (La,Ce,Ca)(Y,U,Fe)(Ti,Fe)20O38\nUranium content: 7.0 - 10.0% U, ~50% TiO2\nComplex metamict titanate ore occurring in high-temperature hydrothermal veins.",
                "Давидит\nФормула: (La,Ce,Ca)(Y,U,Fe)(Ti,Fe)20O38\nСодержание урана: 7.0 - 10.0% U, ~50% TiO2\nСложный титанат урана и редкоземельных металлов из высокотемпературных жил.");

        addItem("ore_samarskite",
                "Samarskite-(Y)\nFormula: (Y,Fe3+,Fe2+,U,Th,Ca)2(Nb,Ta)2O8\nUranium content: 10.0 - 15.0% U, 1.0 - 4.0% Th\nVelvety black metamict niobate-tantalate mineral found in granite pegmatites.",
                "Самарскит\nФормула: (Y,Fe3+,Fe2+,U,Th,Ca)2(Nb,Ta)2O8\nСодержание урана: 10.0 - 15.0% U, 1.0 - 4.0% Th\nЧёрный ниобат-танталат из гранитных пегматитов; назван в честь В. Е. Самарского.");

        addItem("ore_euxenite",
                "Euxenite-(Y)\nFormula: (Y,Ca,Ce,U,Th)(Nb,Ta,Ti)2O6\nUranium content: 5.0 - 12.0% U, 2.0 - 5.0% Th\nComplex rare-earth oxide ore with conchoidal fracture and high metamict decay.",
                "Эвксенит\nФормула: (Y,Ca,Ce,U,Th)(Nb,Ta,Ti)2O6\nСодержание урана: 5.0 - 12.0% U, 2.0 - 5.0% Th\nТитано-тантало-ниобат редких земель с сильным метамиктным распадом структуры.");

        addItem("ore_betafite",
                "Betafite\nFormula: (Ca,U)2(Ti,Nb,Ta)2O6(OH,F)\nUranium content: 15.0 - 25.0% U\nPrimary uranium-rich member of the pyrochlore supergroup in alkaline pegmatites.",
                "Бетафит\nФормула: (Ca,U)2(Ti,Nb,Ta)2O6(OH,F)\nСодержание урана: 15.0 - 25.0% U\nМинерал группы пирохлора; важный источник урана, тантала и ниобия.");

        addItem("ore_fergusonite",
                "Fergusonite-(Y)\nFormula: YNbO4 (with U, Th, REE)\nUranium content: 1.0 - 5.0% U, 1.0 - 3.0% Th\nTetragonal rare-earth niobate occurring in zircon-bearing pegmatite dykes.",
                "Фергюсонит\nФормула: YNbO4 (с примесью U, Th, РЗЭ)\nСодержание урана: 1.0 - 5.0% U, 1.0 - 3.0% Th\nРедкоземельный ниобат иттрия, содержащий изоморфную примесь урана и тория.");

        addItem("ore_thorite",
                "Thorite\nFormula: ThSiO4\nThorium content: 71.6% Th (with up to 10% U)\nPrimary thorium silicate found in pegmatites and placer beach deposits.",
                "Торит\nФормула: ThSiO4\nСодержание тория: 71.6% Th (до 10% урана)\nГлавный первичный силикат тория из пегматитов и прибрежных россыпей.");

        addItem("ore_thorianite",
                "Thorianite\nFormula: ThO2 (with up to 15% UO2)\nThorium content: ~87.9% Th\nExtremely dense (9.7 g/cm3) rare refractory cubic oxide mineral.",
                "Торианит\nФормула: ThO2 (до 15% UO2)\nСодержание тория: ~87.9% Th\nТугоплавкий кристаллический природный оксид тория рекордной плотности (9.7 г/см3).");

        addItem("ore_monazite",
                "Monazite-(Ce)\nFormula: (Ce,La,Nd,Th)PO4\nComposition: 4.0 - 12.0% Th, 0.1 - 1.0% U, 50-60% REE\nHeavy mineral sand source for global industrial thorium extraction.",
                "Монацит\nФормула: (Ce,La,Nd,Th)PO4\nСостав: 4.0 - 12.0% Th, 0.1 - 1.0% U, 50-60% РЗЭ\nФосфат церия и лантаноидов; главное промышленное сырьё для извлечения тория.");

        addItem("ore_xenotime",
                "Xenotime-(Y)\nFormula: YPO4 (with Th, U)\nComposition: 2.0 - 5.0% Th, 1.0 - 4.0% U, 60% Y2O3\nYttrium phosphate ore associated with monazite in zircon-rich heavy sands.",
                "Ксенотим\nФормула: YPO4 (с примесью Th, U)\nСостав: 2.0 - 5.0% Th, 1.0 - 4.0% U, 60% Y2O3\nФосфат иттрия; спутник монацита в радиоактивных цирконовых россыпях.");

        addItem("ore_allanite",
                "Allanite-(Ce) / Orthite\nFormula: (Ca,Ce,La,Y)2(Al,Fe3+)3(SiO4)3(OH)\nThorium content: 0.5 - 3.5% Th\nEpidote-group radioactive sorosilicate found in calc-silicate skarns and granites.",
                "Алланит (Ортит)\nФормула: (Ca,Ce,La,Y)2(Al,Fe3+)3(SiO4)3(OH)\nСодержание тория: 0.5 - 3.5% Th\nРадиоактивный минерал группы эпидота; встречается в скарнах и гранитоидах.");

        addItem("ore_loparite",
                "Loparite-(Ce)\nFormula: (Ce,Na,Ca)(Ti,Nb)O3 (with Th)\nThorium content: 0.5 - 2.0% Th\nBlack granular perovskite ore found in layered agpaitic nepheline syenites.",
                "Лопарит\nФормула: (Ce,Na,Ca)(Ti,Nb)O3 (с примесью Th)\nСодержание тория: 0.5 - 2.0% Th\nТитанониобат группы перовскита из щелочных массивов Кольского полуострова.");

        addItem("ore_uranothorite",
                "Uranothorite\nFormula: (Th,U)SiO4\nComposition: 40.0 - 50.0% Th, 10.0 - 25.0% U\nUranium-rich variety of thorite with severe structural metamictization.",
                "Ураноторит\nФормула: (Th,U)SiO4\nСостав: 40.0 - 50.0% Th, 10.0 - 25.0% U\nУранистый силикат тория с сильным радиационным разрушением кристаллической решётки.");

        addItem("ore_ningyoite",
                "Ningyoite\nFormula: CaU(PO4)2 * 2H2O\nUranium content: ~45.2% U\nBrown microcrystalline secondary phosphate mineral in sedimentary roll-fronts.",
                "Нингёит\nФормула: CaU(PO4)2 * 2H2O\nСодержание урана: ~45.2% U\nМикрокристаллический водный уранил-фосфат кальция из песчаниковых руд.");

        // =========================================================================
        // 12. ВТОРИЧНЫЕ МИНЕРАЛЫ ЗОН ОКИСЛЕНИЯ (MINERALS)
        // =========================================================================
        addItem("mineral_carnotite",
                "Carnotite\nFormula: K2(UO2)2(VO4)2 * 3H2O\nComposition: 52.8 - 55.0% U, 11.4% V, 8.7% K\nBright canary-yellow potassium uranyl vanadate in sandstone horizons.",
                "Карнотит\nФормула: K2(UO2)2(VO4)2 * 3H2O\nСостав: 52.8 - 55.0% U, 11.4% V, 8.7% K\nЯрко-жёлтый вторичный уранил-ванадат калия; ключевой минерал плато Колорадо.");

        addItem("mineral_tyuyamunite",
                "Tyuyamunite\nFormula: Ca(UO2)2(VO4)2 * (5-8)H2O\nComposition: 51.7 - 54.5% U, 11.1% V, 4.4% Ca\nCalcium analogue of carnotite first described in Tyuya-Muyun, Central Asia.",
                "Тюямунит\nФормула: Ca(UO2)2(VO4)2 * (5-8)H2O\nСостав: 51.7 - 54.5% U, 11.1% V, 4.4% Ca\nКальциевый аналог карнотита. Впервые описан в урочище Тюя-Муюн.");

        addItem("mineral_autunite",
                "Autunite\nFormula: Ca(UO2)2(PO4)2 * 10-12H2O\nComposition: 48.3 - 50.8% U, 6.3% P, 4.1% Ca\nGreenish-yellow tabular crystals exhibiting bright neon-green UV fluorescence.",
                "Отенит\nФормула: Ca(UO2)2(PO4)2 * 10-12H2O\nСостав: 48.3 - 50.8% U, 6.3% P, 4.1% Ca\nСлюдоподобный уранил-фосфат кальция с ярчайшей изумрудной УФ-флуоресценцией.");

        addItem("mineral_torbernite",
                "Torbernite\nFormula: Cu(UO2)2(PO4)2 * 12H2O\nComposition: 47.1% U, 6.1% Cu, 6.0% P\nEmerald-green square tabular crystals; does not fluoresce due to copper quenching.",
                "Торбернит\nФормула: Cu(UO2)2(PO4)2 * 12H2O\nСостав: 47.1% U, 6.1% Cu, 6.0% P\nИзумрудно-зелёный медный уранил-фосфат; тушит флуоресценцию из-за ионов меди.");

        addItem("mineral_uranophane",
                "Uranophane\nFormula: Ca(UO2)2(SiO3OH)2 * 5H2O\nComposition: 55.6% U, 6.6% Si, 4.7% Ca\nStraw-yellow acicular silicate formed by weathering of uraninite veins.",
                "Уранофан\nФормула: Ca(UO2)2(SiO3OH)2 * 5H2O\nСостав: 55.6% U, 6.6% Si, 4.7% Ca\nИгольчатый соломенно-жёлтый ураносиликат в зонах выветривания уранинитовых жил.");

        addItem("mineral_saleeite",
                "Saleeite\nFormula: Mg(UO2)2(PO4)2 * 10H2O\nComposition: 50.9% U, 6.6% P, 2.6% Mg\nMagnesium member of the autunite group showing bright lemon-yellow fluorescence.",
                "Салеит\nФормула: Mg(UO2)2(PO4)2 * 10H2O\nСостав: 50.9% U, 6.6% P, 2.6% Mg\nМагниевый уранил-фосфат с ярким лимонным свечением в лучах ультрафиолета.");

        addItem("mineral_zeunerite",
                "Zeunerite\nFormula: Cu(UO2)2(AsO4)2 * (10-16)H2O\nComposition: 45.1% U, 14.2% As, 6.0% Cu\nEmerald-green arsenate analogue of torbernite found in arsenic-rich veins.",
                "Цейнерит\nФормула: Cu(UO2)2(AsO4)2 * (10-16)H2O\nСостав: 45.1% U, 14.2% As, 6.0% Cu\nАрсенатный изумрудно-зелёный аналог торбернита из мышьяковистых гидротермальных жил.");

        addItem("mineral_curite",
                "Curite\nFormula: Pb3(UO2)8O8(OH)6 * 3H2O\nComposition: 62.5% U, 21.3% Pb\nReddish-orange hydrous lead uranyl oxide named after Marie Curie.",
                "Кюрит\nФормула: Pb3(UO2)8O8(OH)6 * 3H2O\nСостав: 62.5% U, 21.3% Pb\nОранжево-красный свинцовый гидроксид-оксид уранила; назван в честь Марии Кюри.");

        addItem("mineral_becquerelite",
                "Becquerelite\nFormula: Ca(UO2)6O4(OH)6 * 8H2O\nComposition: 72.5% U, 2.0% Ca\nAmber-yellow hydrated uranyl oxide mineral possessing exceptional uranium density.",
                "Беккерелит\nФормула: Ca(UO2)6O4(OH)6 * 8H2O\nСостав: 72.5% U, 2.0% Ca\nЯнтарно-жёлтый минерал с рекордно высокой концентрацией урана для вторичных руд.");

        addItem("mineral_sklodowskite",
                "Sklodowskite\nFormula: Mg(UO2)2(SiO3OH)2 * 6H2O\nComposition: 54.6% U, 6.4% Si, 2.8% Mg\nPale greenish-yellow acicular magnesium uranyl silicate.",
                "Склодовскит\nФормула: Mg(UO2)2(SiO3OH)2 * 6H2O\nСостав: 54.6% U, 6.4% Si, 2.8% Mg\nИгольчатый магниевый ураносиликат; назван в честь Марии Склодовской-Кюри.");

        addItem("mineral_cuprosklodowskite",
                "Cuprosklodowskite\nFormula: Cu(UO2)2(SiO3OH)2 * 6H2O\nComposition: 53.1% U, 7.1% Cu, 6.3% Si\nGrass-green fibrous radial clusters formed by copper-uranium oxidation.",
                "Купросклодовскит\nФормула: Cu(UO2)2(SiO3OH)2 * 6H2O\nСостав: 53.1% U, 7.1% Cu, 6.3% Si\nТравянисто-зелёный шелковистый минерал радиально-лучистого строения.");

        addItem("mineral_kasolite",
                "Kasolite\nFormula: Pb(UO2)SiO4 * H2O\nComposition: 49.4% U, 37.8% Pb, 4.9% Si\nOchre-yellow monoclinic lead uranyl silicate of the oxidation zone.",
                "Казолит\nФормула: Pb(UO2)SiO4 * H2O\nСостав: 49.4% U, 37.8% Pb, 4.9% Si\nОхристо-жёлтый свинцовый ураносиликат; продукт распада радиогенного свинца.");

        addItem("mineral_parsonsite",
                "Parsonsite\nFormula: Pb2(UO2)(PO4)2 * 2H2O\nComposition: 27.3% U, 47.6% Pb, 7.1% P\nPale yellow to brown lead uranyl phosphate occurring in compact fibrous crusts.",
                "Парсонсит\nФормула: Pb2(UO2)(PO4)2 * 2H2O\nСостав: 27.3% U, 47.6% Pb, 7.1% P\nСвинцово-урановый фосфат; образует радиально-лучистые корки выветривания.");

        addItem("mineral_boltwoodite",
                "Boltwoodite\nFormula: K(UO2)(SiO3OH) * 1.5H2O\nComposition: 57.0% U, 9.4% K, 6.7% Si\nCanary-yellow silicate mineral named after nuclear physicist Bertram Boltwood.",
                "Болтвудит\nФормула: K(UO2)(SiO3OH) * 1.5H2O\nСостав: 57.0% U, 9.4% K, 6.7% Si\nКалиевый ураносиликат; назван в честь первооткрывателя радиогенного свинца Б. Болтвуда.");

        addItem("mineral_liebigite",
                "Liebigite\nFormula: Ca2(UO2)(CO3)3 * 11H2O\nComposition: 34.8% U, 11.7% Ca, 5.3% C\nApple-green highly soluble uranyl carbonate displaying brilliant green fluorescence.",
                "Либигит\nФормула: Ca2(UO2)(CO3)3 * 11H2O\nСостав: 34.8% U, 11.7% Ca, 5.3% C\nЯблочно-зелёный легкорастворимый карбонат уранила с ярчайшей флуоресценцией.");

        addItem("mineral_schrockingerite",
                "Schrockingerite\nFormula: NaCa3(UO2)(CO3)3(SO4)F * 10H2O\nComposition: 26.8% U, 13.5% Ca, 2.6% Na, 3.6% S\nComplex greenish-yellow carbonate-sulfate-fluoride forming efflorescent surface crusts.",
                "Шрёкингерит\nФормула: NaCa3(UO2)(CO3)3(SO4)F * 10H2O\nСостав: 26.8% U, 13.5% Ca, 2.6% Na, 3.6% S\nСложный карбонато-сульфат уранила, выцветающий на стенках горных выработок.");

        addItem("mineral_andersonite",
                "Andersonite\nFormula: Na2Ca(UO2)(CO3)3 * 6H2O\nComposition: 37.3% U, 7.2% Na, 6.3% Ca\nBright yellow-green evaporitic uranyl carbonate with blazing neon fluorescence.",
                "Андерсонит\nФормула: Na2Ca(UO2)(CO3)3 * 6H2O\nСостав: 37.3% U, 7.2% Na, 6.3% Ca\nЭвапоритовый карбонат уранила, светящийся в темноте под УФ-фонарём.");

        addItem("mineral_billietite",
                "Billietite\nFormula: Ba(UO2)6O4(OH)6 * 8H2O\nComposition: 68.8% U, 6.6% Ba\nAmber-yellow barium uranyl oxide structurally related to becquerelite.",
                "Биллиетит\nФормула: Ba(UO2)6O4(OH)6 * 8H2O\nСостав: 68.8% U, 6.6% Ba\nБариевый гидроксид-оксид уранила; редкий спутник уранинита в пегматитах.");

        addItem("mineral_francevillite",
                "Francevillite\nFormula: Ba(UO2)2(VO4)2 * 5H2O\nComposition: 49.2% U, 14.2% Ba, 10.5% V\nOrange-yellow vanadate ore discovered at Mounana, Gabon.",
                "Франсевиллит\nФормула: Ba(UO2)2(VO4)2 * 5H2O\nСостав: 49.2% U, 14.2% Ba, 10.5% V\nОранжево-жёлтый бариевый уранил-ванадат из знаменитого бассейна Франсвиль (Габон).");

        addItem("mineral_uranosphaerite",
                "Uranosphaerite\nFormula: Bi2O3 * 2UO3 * 3H2O\nComposition: 43.7% U, 38.3% Bi\nReddish-brown spherical crystalline aggregates of bismuth uranyl oxide.",
                "Ураносферит\nФормула: Bi2O3 * 2UO3 * 3H2O\nСостав: 43.7% U, 38.3% Bi\nВисмутовый оксигидрат уранила; образует полусферические агрегаты в жилах.");

        // =========================================================================
        // 13. ХИМИЧЕСКИЕ СОЕДИНЕНИЯ: ОКСИДЫ, ГИДРОКСИДЫ, УРАНАТЫ
        // =========================================================================
        addItem("u_dioxide",
                "Uranium Dioxide (UO2)\nFormula: UO2 (Molar mass: 270.03 g/mol)\nComposition: 88.15% U, 11.85% O\nThe workhorse ceramic fuel for commercial nuclear reactors (PWR, BWR, VVER).",
                "Диоксид урана (UO2)\nФормула: UO2 (Молярная масса: 270.03 г/моль)\nСостав: 88.15% U, 11.85% O\nГлавное керамическое ядерное топливо мировых энергетических реакторов.");

        addItem("u_trioxide",
                "Uranium Trioxide (UO3)\nFormula: UO3 (Molar mass: 286.03 g/mol)\nComposition: 83.22% U, 16.78% O\nOrange-yellow hexavalent oxide; key intermediate during PUREX reprocessing.",
                "Триоксид урана (UO3)\nФормула: UO3 (Молярная масса: 286.03 г/моль)\nСостав: 83.22% U, 16.78% O\nОранжево-жёлтый оксид шестивалентного урана в технологии PUREX.");

        addItem("u_octaoxide",
                "Triuranium Octaoxide (U3O8)\nFormula: U3O8 (Molar mass: 842.09 g/mol)\nComposition: 84.80% U, 15.20% O\nThe most thermally stable uranium oxide; standard form for strategic stockpiles.",
                "Закись-окись урана (U3O8)\nФормула: U3O8 (Молярная масса: 842.09 г/моль)\nСостав: 84.80% U, 15.20% O\nСамый термодинамически устойчивый оксид урана; форма складского хранения.");

        addItem("uranyl_peroxide",
                "Uranyl Peroxide (UO4 * 2H2O)\nFormula: UO4 * 2H2O (Molar mass: 338.06 g/mol)\nComposition: 70.41% U, 28.39% O, 1.20% H\nDense yellow precipitate obtained by adding hydrogen peroxide during refining.",
                "Пероксид уранила (UO4 * 2H2O)\nФормула: UO4 * 2H2O (Молярная масса: 338.06 г/моль)\nСостав: 70.41% U, 28.39% O, 1.20% H\nЖёлтый кристаллический осадок перекисного аффинажа урановых концентратов.");

        addItem("th_dioxide",
                "Thorium Dioxide (ThO2)\nFormula: ThO2 (Molar mass: 264.04 g/mol)\nComposition: 87.88% Th, 12.12% O\nHighest melting-point oxide known (3390°C); fertile breeder fuel for thorium reactors.",
                "Диоксид тория (ThO2)\nФормула: ThO2 (Молярная масса: 264.04 г/моль)\nСостав: 87.88% Th, 12.12% O\nРекордная температура плавления (3390°C); сырьевое топливо ториевого цикла.");

        addItem("pu_dioxide",
                "Plutonium Dioxide (PuO2)\nFormula: PuO2 (Molar mass: 271.05 g/mol)\nComposition: 88.19% Pu, 11.81% O\nOlive-green ceramic compound; core component of MOX fuels and space RTGs.",
                "Диоксид плутония (PuO2)\nФормула: PuO2 (Молярная масса: 271.05 г/моль)\nСостав: 88.19% Pu, 11.81% O\nОливково-зелёная керамика; основа MOX-топлива и космических РИТЭГ.");

        addItem("pu_sesquioxide",
                "Plutonium Sesquioxide (Pu2O3)\nFormula: Pu2O3 (Molar mass: 526.10 g/mol)\nComposition: 90.88% Pu, 9.12% O\nPyrophoric black sub-oxide formed under severe high-temperature reduction.",
                "Сесквиоксид плутония (Pu2O3)\nФормула: Pu2O3 (Молярная масса: 526.10 г/моль)\nСостав: 90.88% Pu, 9.12% O\nЧёрный пирофорный низший оксид плутония восстановительной плавки.");

        addItem("np_dioxide",
                "Neptunium Dioxide (NpO2)\nFormula: NpO2 (Molar mass: 269.05 g/mol)\nComposition: 88.11% Np, 11.89% O\nGreenish-brown actinide powder target irradiated to produce pure Pu-238.",
                "Диоксид нептуния (NpO2)\nФормула: NpO2 (Молярная масса: 269.05 г/моль)\nСостав: 88.11% Np, 11.89% O\nРеакторная мишень нейтронного облучения для наработки чистого Плутония-238.");

        addItem("am_dioxide",
                "Americium Dioxide (AmO2)\nFormula: AmO2 (Molar mass: 273.06 g/mol)\nComposition: 88.28% Am, 11.72% O\nDark brown alpha-emitter oxide utilized inside ionization smoke detectors.",
                "Диоксид америция (AmO2)\nФормула: AmO2 (Молярная масса: 273.06 г/моль)\nСостав: 88.28% Am, 11.72% O\nТёмно-коричневый альфа-источник ионизационных камер детекторов задымления.");

        addItem("cm_sesquioxide",
                "Curium Sesquioxide (Cm2O3)\nFormula: Cm2O3 (Molar mass: 542.12 g/mol)\nComposition: 90.41% Cm, 9.59% O\nRadioluminescent white oxide that glows red-hot due to intense alpha-self-heating.",
                "Сесквиоксид кюрия (Cm2O3)\nФормула: Cm2O3 (Молярная масса: 542.12 г/моль)\nСостав: 90.41% Cm, 9.59% O\nОксид с колоссальным альфа-тепловыделением, светящийся в темноте докрасна.");

        addItem("po_dioxide",
                "Polonium Dioxide (PoO2)\nFormula: PoO2 (Molar mass: 241.98 g/mol)\nComposition: 86.78% Po, 13.22% O\nExtremely volatile, heat-producing radiotoxin capable of decomposing its own lattice.",
                "Диоксид полония (PoO2)\nФормула: PoO2 (Молярная масса: 241.98 г/моль)\nСостав: 86.78% Po, 13.22% O\nЛетучий сверхтоксичный оксид с огромной тепловой и радиационной мощностью.");

        addItem("ra_oxide",
                "Radium Oxide (RaO)\nFormula: RaO (Molar mass: 242.03 g/mol)\nComposition: 93.39% Ra, 6.61% O\nWhite hygroscopic alkaline-earth oxide reacting violently with moisture to emit radon.",
                "Оксид радия (RaO)\nФормула: RaO (Молярная масса: 242.03 г/моль)\nСостав: 93.39% Ra, 6.61% O\nБелый гигроскопичный щёлочноземельный оксид, бурно выделяющий радон.");

        addItem("uranyl_hydroxide",
                "Uranyl Hydroxide (UO2(OH)2)\nFormula: UO2(OH)2 (Molar mass: 304.05 g/mol)\nComposition: 78.29% U, 21.05% O, 0.66% H\nAmphoteric yellow solid precipitated from aqueous uranyl nitrate solutions.",
                "Гидроксид уранила (UO2(OH)2)\nФормула: UO2(OH)2 (Молярная масса: 304.05 г/моль)\nСостав: 78.29% U, 21.05% O, 0.66% H\nАмфотерный жёлтый гидроксид, осаждаемый из водных растворов нитрата уранила.");

        addItem("th_hydroxide",
                "Thorium Hydroxide (Th(OH)4)\nFormula: Th(OH)4 (Molar mass: 300.07 g/mol)\nComposition: 77.33% Th, 21.33% O, 1.34% H\nWhite gelatinous precipitate formed during thorium caustic leaching.",
                "Гидроксид тория (Th(OH)4)\nФормула: Th(OH)4 (Молярная масса: 300.07 г/моль)\nСостав: 77.33% Th, 21.33% O, 1.34% H\nБелый студенистый осадок щелочного выщелачивания ториевых концентратов.");

        addItem("pu_hydroxide",
                "Plutonium(IV) Hydroxide (Pu(OH)4)\nFormula: Pu(OH)4 (Molar mass: 307.08 g/mol)\nComposition: 77.85% Pu, 20.84% O, 1.31% H\nInsoluble olive polymer precipitate central to plutonium purification cycles.",
                "Гидроксид плутония(IV) (Pu(OH)4)\nФормула: Pu(OH)4 (Молярная масса: 307.08 г/моль)\nСостав: 77.85% Pu, 20.84% O, 1.31% H\nОливковый полимерный осадок, ключевой в химическом аффинаже плутония.");

        addItem("am_hydroxide",
                "Americium(III) Hydroxide (Am(OH)3)\nFormula: Am(OH)3 (Molar mass: 292.08 g/mol)\nComposition: 82.53% Am, 16.43% O, 1.04% H\nPink-tinted radioactive gelatinous precipitate.",
                "Гидроксид америция(III) (Am(OH)3)\nФормула: Am(OH)3 (Молярная масса: 292.08 г/моль)\nСостав: 82.53% Am, 16.43% O, 1.04% H\nРозоватый радиоактивный осадок гидролиза трёхвалентного америция.");

        addItem("ra_hydroxide",
                "Radium Hydroxide (Ra(OH)2)\nFormula: Ra(OH)2 (Molar mass: 260.05 g/mol)\nComposition: 86.92% Ra, 12.31% O, 0.77% H\nCaustic, strongly alkaline, and intensely radioactive alkaline-earth base.",
                "Гидроксид радия (Ra(OH)2)\nФормула: Ra(OH)2 (Молярная масса: 260.05 г/моль)\nСостав: 86.92% Ra, 12.31% O, 0.77% H\nЕдкое радиоактивное основание щелочноземельной группы высокой растворимости.");

        addItem("ammonium_diuranate",
                "Ammonium Diuranate (ADU)\nFormula: (NH4)2U2O7 (Molar mass: 624.14 g/mol)\nComposition: 76.27% U, 17.94% O, 4.49% N, 1.30% H\nClassic 'Yellowcake' precursor calcined to produce reactor-grade UO2.",
                "Диуранат аммония (Жёлтый кек / ADU)\nФормула: (NH4)2U2O7 (Молярная масса: 624.14 г/моль)\nСостав: 76.27% U, 17.94% O, 4.49% N, 1.30% H\nПромышленный жёлтый кек, прокаливаемый в реакторный диоксид урана.");

        addItem("sodium_diuranate",
                "Sodium Diuranate (SDU)\nFormula: Na2U2O7 (Molar mass: 633.99 g/mol)\nComposition: 75.09% U, 17.67% O, 7.24% Na\nBright yellow salt historically favored for producing fluorescent Uranium Glass.",
                "Диуранат натрия (SDU)\nФормула: Na2U2O7 (Молярная масса: 633.99 г/моль)\nСостав: 75.09% U, 17.67% O, 7.24% Na\nУрановый краситель, применявшийся для варки светящегося уранового стекла.");

        addItem("magnesium_diuranate",
                "Magnesium Diuranate (MDU)\nFormula: MgU2O7 (Molar mass: 611.92 g/mol)\nComposition: 77.80% U, 18.23% O, 3.97% Mg\nRefined yellow cake product of magnesium hydroxide precipitation.",
                "Диуранат магния (MDU)\nФормула: MgU2O7 (Молярная масса: 611.92 г/моль)\nСостав: 77.80% U, 18.23% O, 3.97% Mg\nМагниевый жёлтый кек, осаждаемый гидроксидом магния на обогатительных фабриках.");

        // =========================================================================
        // 14. ХИМИЧЕСКИЕ СОЕДИНЕНИЯ: СОЛИ, КАРБИДЫ, НИТРИДЫ
        // =========================================================================
        addItem("u_hexafluoride",
                "Uranium Hexafluoride (UF6)\nFormula: UF6 (Molar mass: 352.02 g/mol)\nComposition: 67.61% U, 32.39% F\nVolatile white crystals (sublimes at 56.5°C). The feed gas for enrichment centrifuges.",
                "Гексафторид урана (UF6)\nФормула: UF6 (Молярная масса: 352.02 г/моль)\nСостав: 67.61% U, 32.39% F\nЛетучие белые кристаллы (сублимация при 56.5°C). Рабочий газ газовых центрифуг.");

        addItem("u_tetrafluoride",
                "Uranium Tetrafluoride (UF4)\nFormula: UF4 (Green Salt, Molar mass: 314.02 g/mol)\nComposition: 75.80% U, 24.20% F\nEmerald-green crystalline salt reduced with calcium/magnesium into metallic uranium.",
                "Тетрафторид урана (Зелёная соль / UF4)\nФормула: UF4 (Молярная масса: 314.02 г/моль)\nСостав: 75.80% U, 24.20% F\nИзумрудно-зелёный порошок; сырьё металлотермической выплавки металлического урана.");

        addItem("u_tetrachloride",
                "Uranium Tetrachloride (UCl4)\nFormula: UCl4 (Molar mass: 379.84 g/mol)\nComposition: 62.67% U, 37.33% Cl\nHygroscopic dark green crystalline reagent for non-aqueous pyrochemical fuel recycling.",
                "Тетрахлорид урана (UCl4)\nФормула: UCl4 (Молярная масса: 379.84 г/моль)\nСостав: 62.67% U, 37.33% Cl\nТёмно-зелёные гигроскопичные кристаллы для пирохимической переработки ОЯТ.");

        addItem("u_hexachloride",
                "Uranium Hexachloride (UCl6)\nFormula: UCl6 (Molar mass: 450.74 g/mol)\nComposition: 52.81% U, 47.19% Cl\nDark green sublimate; volatile actinide halide decomposed easily by moisture.",
                "Гексахлорид урана (UCl6)\nФормула: UCl6 (Молярная масса: 450.74 г/моль)\nСостав: 52.81% U, 47.19% Cl\nТёмно-зелёный летучий высший хлорид урана, легко гидролизующийся на воздухе.");

        addItem("u_tetrabromide",
                "Uranium Tetrabromide (UBr4)\nFormula: UBr4 (Molar mass: 557.65 g/mol)\nComposition: 42.68% U, 57.32% Br\nBrown deliquescent crystalline salt prepared for inorganic actinide research.",
                "Тетрабромид урана (UBr4)\nФормула: UBr4 (Молярная масса: 557.65 г/моль)\nСостав: 42.68% U, 57.32% Br\nКоричневые расплывающиеся на воздухе кристаллы четырёхвалентного урана.");

        addItem("u_tetraiodide",
                "Uranium Tetraiodide (UI4)\nFormula: UI4 (Molar mass: 745.65 g/mol)\nComposition: 31.92% U, 68.08% I\nBlack needle-like crystals used for producing ultra-high purity metallic uranium via van Arkel de Boer.",
                "Тетраиодид урана (UI4)\nФормула: UI4 (Молярная масса: 745.65 г/моль)\nСостав: 31.92% U, 68.08% I\nЧёрные игольчатые кристаллы; реагент иодидного рафинирования урана особой чистоты.");

        addItem("uranyl_nitrate",
                "Uranyl Nitrate Hexahydrate (UNH)\nFormula: UO2(NO3)2 * 6H2O (Molar mass: 502.13 g/mol)\nComposition: 47.41% U, 50.19% O, 5.58% N, 2.41% H\nBrilliant yellow-green fluorescent crystals. Primary liquid phase in PUREX extraction.",
                "Нитрат уранила гексагидрат (UNH)\nФормула: UO2(NO3)2 * 6H2O (Молярная масса: 502.13 г/моль)\nСостав: 47.41% U, 50.19% O, 5.58% N, 2.41% H\nЛимонно-жёлтые флуоресцентные кристаллы экстракционного процесса PUREX.");

        addItem("uranyl_sulfate",
                "Uranyl Sulfate Trihydrate\nFormula: UO2SO4 * 3H2O (Molar mass: 420.15 g/mol)\nComposition: 56.65% U, 38.08% O, 7.63% S, 1.44% H\nLiquid fuel solution utilized inside Aqueous Homogeneous Nuclear Reactors (AHR).",
                "Сульфат уранила тригидрат\nФормула: UO2SO4 * 3H2O (Молярная масса: 420.15 г/моль)\nСостав: 56.65% U, 38.08% O, 7.63% S, 1.44% H\nЖидкое ядерное топливо водных гомогенных исследовательских реакторов.");

        addItem("uranyl_acetate",
                "Uranyl Acetate Dihydrate\nFormula: UO2(CH3COO)2 * 2H2O (Molar mass: 424.15 g/mol)\nComposition: 56.12% U, 30.18% O, 11.32% C, 2.38% H\nStandard heavy-metal contrast stain for transmission electron microscopy (TEM).",
                "Ацетат уранила дигидрат\nФормула: UO2(CH3COO)2 * 2H2O (Молярная масса: 424.15 г/моль)\nСостав: 56.12% U, 30.18% O, 11.32% C, 2.38% H\nЭталонный контрастный краситель для просвечивающей электронной микроскопии (ПЭМ).");

        addItem("uranyl_carbonate",
                "Uranyl Carbonate (UO2CO3)\nFormula: UO2CO3 (Molar mass: 330.04 g/mol)\nComposition: 72.12% U, 24.24% O, 3.64% C\nInsoluble mineral salt (rutherfordine) forming soluble tricarbonato complexes in groundwater.",
                "Карбонат уранила (Резерфордин / UO2CO3)\nФормула: UO2CO3 (Молярная масса: 330.04 г/моль)\nСостав: 72.12% U, 24.24% O, 3.64% C\nМинеральная соль уранила; определяет миграцию урана в природных карбонатных водах.");

        addItem("u_monocarbide",
                "Uranium Monocarbide (UC)\nFormula: UC (Molar mass: 250.04 g/mol)\nComposition: 95.20% U, 4.80% C\nHigh thermal conductivity ceramic fuel for generation-IV fast breeder reactors.",
                "Монокарбид урана (UC)\nФормула: UC (Молярная масса: 250.04 г/моль)\nСостав: 95.20% U, 4.80% C\nВысокотеплопроводное керамическое топливо реакторов на быстрых нейтронах IV поколения.");

        addItem("u_dicarbide",
                "Uranium Dicarbide (UC2)\nFormula: UC2 (Molar mass: 262.05 g/mol)\nComposition: 90.84% U, 9.16% C\nRefractory kernel core for TRISO micro-spherical fuel particles in HTGR reactors.",
                "Дикарбид урана (UC2)\nФормула: UC2 (Молярная масса: 262.05 г/моль)\nСостав: 90.84% U, 9.16% C\nТугоплавкие топливные микросферы микротвэлов TRISO высокотемпературных реакторов.");

        addItem("u_mononitride",
                "Uranium Mononitride (UN)\nFormula: UN (Molar mass: 252.03 g/mol)\nComposition: 94.44% U, 5.56% N\nDense, high-thermal-margin advanced fast reactor fuel with excellent fissile density.",
                "Мононитрид урана (UN)\nФормула: UN (Молярная масса: 252.03 г/моль)\nСостав: 94.44% U, 5.56% N\nПередовое плотное нитридное топливо (СНУП) для реакторов БРЕСТ-ОД-300.");

        addItem("th_tetrafluoride",
                "Thorium Tetrafluoride (ThF4)\nFormula: ThF4 (Molar mass: 308.03 g/mol)\nComposition: 75.33% Th, 24.67% F\nMolten salt reactor (LFTR) component dissolved in FLiBe eutectic carrier salts.",
                "Тетрафторид тория (ThF4)\nФормула: ThF4 (Молярная масса: 308.03 г/моль)\nСостав: 75.33% Th, 24.67% F\nТопливный компонент жидкосолевых ториевых реакторов на эвтектике FLiBe.");

        addItem("th_tetrachloride",
                "Thorium Tetrachloride (ThCl4)\nFormula: ThCl4 (Molar mass: 373.85 g/mol)\nComposition: 62.07% Th, 37.93% Cl\nWhite hygroscopic crystals used for non-aqueous pyrometallurgical reduction to thorium metal.",
                "Тетрахлорид тория (ThCl4)\nФормула: ThCl4 (Молярная масса: 373.85 г/моль)\nСостав: 62.07% Th, 37.93% Cl\nГигроскопичные кристаллы; сырьё для электролитического получения чистого металлического тория.");

        addItem("th_nitrate",
                "Thorium Nitrate Tetrahydrate\nFormula: Th(NO3)4 * 4H2O (Molar mass: 552.12 g/mol)\nComposition: 42.03% Th, 46.37% O, 10.15% N, 1.46% H\nSoluble white salt historically used to impregnate Welsbach incandescent gas mantles.",
                "Нитрат тория тетрагидрат\nФормула: Th(NO3)4 * 4H2O (Молярная масса: 552.12 г/моль)\nСостав: 42.03% Th, 46.37% O, 10.15% N, 1.46% H\nБелая соль, исторически использовавшаяся для пропитки газокалильных сеток Ауэра.");

        addItem("th_dicarbide",
                "Thorium Dicarbide (ThC2)\nFormula: ThC2 (Molar mass: 256.06 g/mol)\nComposition: 90.62% Th, 9.38% C\nHigh-temperature refractory fertile carbide fuel (m.p. 2655°C).",
                "Дикарбид тория (ThC2)\nФормула: ThC2 (Молярная масса: 256.06 г/моль)\nСостав: 90.62% Th, 9.38% C\nВысокотемпературная топливная керамика ториевого цикла с температурой плавления 2655°C.");

        addItem("pu_trifluoride",
                "Plutonium Trifluoride (PuF3)\nFormula: PuF3 (Molar mass: 296.04 g/mol)\nComposition: 80.75% Pu, 19.25% F\nPurple-violet crystalline salt used in molten salt reactor fissile compositions.",
                "Трифторид плутония (PuF3)\nФормула: PuF3 (Молярная масса: 296.04 г/моль)\nСостав: 80.75% Pu, 19.25% F\nФиолетовая соль трёхвалентного плутония; компонент делящихся смесей ЖСР.");

        addItem("pu_tetrafluoride",
                "Plutonium Tetrafluoride (PuF4)\nFormula: PuF4 (Molar mass: 315.04 g/mol)\nComposition: 75.88% Pu, 24.12% F\nPink-brown powder reduced via calcium thermite reaction to yield metallic plutonium pits.",
                "Тетрафторид плутония (PuF4)\nФормула: PuF4 (Молярная масса: 315.04 г/моль)\nСостав: 75.88% Pu, 24.12% F\nРозовато-коричневая соль; сырьё кальциетермической выплавки оружейного плутония.");

        addItem("pu_hexafluoride",
                "Plutonium Hexafluoride (PuF6)\nFormula: PuF6 (Molar mass: 353.04 g/mol)\nComposition: 67.71% Pu, 32.29% F\nVolatile red-brown crystals (b.p. 62°C) highly prone to intense radiolytic self-decomposition.",
                "Гексафторид плутония (PuF6)\nФормула: PuF6 (Молярная масса: 353.04 г/моль)\nСостав: 67.71% Pu, 32.29% F\nЛетучие красно-коричневые кристаллы, саморазлагающиеся от собственного альфа-излучения.");

        addItem("pu_trichloride",
                "Plutonium Trichloride (PuCl3)\nFormula: PuCl3 (Molar mass: 345.41 g/mol)\nComposition: 69.21% Pu, 30.79% Cl\nEmerald-green hygroscopic salt; key electrolyte for electrorefining weapons-grade plutonium.",
                "Трихлорид плутония (PuCl3)\nФормула: PuCl3 (Молярная масса: 345.41 г/моль)\nСостав: 69.21% Pu, 30.79% Cl\nИзумрудно-зелёный расплавный электролит для электролитического рафинирования плутония.");

        addItem("pu_nitrate",
                "Plutonium(IV) Nitrate Pentahydrate\nFormula: Pu(NO3)4 * 5H2O (Molar mass: 577.13 g/mol)\nComposition: 41.42% Pu, 47.13% O, 9.71% N, 1.75% H\nDark green solution; standard transport chemical state in PUREX separations.",
                "Нитрат плутония(IV) пентагидрат\nФормула: Pu(NO3)4 * 5H2O (Молярная масса: 577.13 г/моль)\nСостав: 41.42% Pu, 47.13% O, 9.71% N, 1.75% H\nТёмно-зелёная соль, основной химический раствор транспортировки переработанного плутония.");

        addItem("pu_monocarbide",
                "Plutonium Monocarbide (PuC)\nFormula: PuC (Molar mass: 251.05 g/mol)\nComposition: 95.22% Pu, 4.78% C\nBlack metallic ceramic component in mixed carbide (MC) advanced nuclear fuels.",
                "Монокарбид плутония (PuC)\nФормула: PuC (Молярная масса: 251.05 г/моль)\nСостав: 95.22% Pu, 4.78% C\nЧёрная металлокерамика; компонент смешанного карбидного топлива быстрых реакторов.");

        addItem("pu_mononitride",
                "Plutonium Mononitride (PuN)\nFormula: PuN (Molar mass: 253.05 g/mol)\nComposition: 94.47% Pu, 5.53% N\nGolden-brown refractory nitride with high thermal conductivity (m.p. 2770°C).",
                "Мононитрид плутония (PuN)\nФормула: PuN (Молярная масса: 253.05 г/моль)\nСостав: 94.47% Pu, 5.53% N\nЗолотисто-коричневая нитридная керамика высокой плотности; основа топлива МНУП.");

        addItem("ra_chloride",
                "Radium Chloride (RaCl2)\nFormula: RaCl2 (Molar mass: 296.94 g/mol)\nComposition: 76.12% Ra, 23.88% Cl\nThe historic first pure compound isolated by Marie and Pierre Curie in 1898. Luminescent.",
                "Хлорид радия (RaCl2)\nФормула: RaCl2 (Молярная масса: 296.94 г/моль)\nСостав: 76.12% Ra, 23.88% Cl\nПервое чистое соединение радия, открытое супругами Кюри в 1898 году. Светится в темноте.");

        addItem("ra_bromide",
                "Radium Bromide (RaBr2)\nFormula: RaBr2 (Molar mass: 385.84 g/mol)\nComposition: 58.58% Ra, 41.42% Br\nLuminescent crystals used in historic radiotherapeutic applicators and dials.",
                "Бромид радия (RaBr2)\nФормула: RaBr2 (Молярная масса: 385.84 г/моль)\nСостав: 58.58% Ra, 41.42% Br\nСветящаяся соль ранней лучевой терапии и радиолюминесцентных составов.");

        addItem("ra_sulfate",
                "Radium Sulfate (RaSO4)\nFormula: RaSO4 (Molar mass: 322.10 g/mol)\nComposition: 70.17% Ra, 19.87% O, 9.95% S\nThe least soluble radium salt known (Ksp ~ 3.6e-11). Stable chemical burial form.",
                "Сульфат радия (RaSO4)\nФормула: RaSO4 (Молярная масса: 322.10 г/моль)\nСостав: 70.17% Ra, 19.87% O, 9.95% S\nНаименее растворимое соединение радия (Ksp ~ 3.6e-11). Форма безопасного захоронения.");

        addItem("ra_carbonate",
                "Radium Carbonate (RaCO3)\nFormula: RaCO3 (Molar mass: 286.03 g/mol)\nComposition: 79.02% Ra, 16.78% O, 4.20% C\nInsoluble white precipitate converted into halides during Curie separation schemes.",
                "Карбонат радия (RaCO3)\nФормула: RaCO3 (Молярная масса: 286.03 г/моль)\nСостав: 79.02% Ra, 16.78% O, 4.20% C\nНерастворимый карбонат; промежуточная соль фракционной кристаллизации супругов Кюри.");

        addItem("cs137_chloride",
                "Caesium-137 Chloride (137CsCl)\nFormula: 137CsCl (Molar mass: 172.36 g/mol)\nComposition: 79.43% 137Cs, 20.57% Cl\nExtremely dangerous high-activity gamma source used for commercial medical irradiators.",
                "Хлорид цезия-137 (137CsCl)\nФормула: 137CsCl (Молярная масса: 172.36 г/моль)\nСостав: 79.43% 137Cs, 20.57% Cl\nВысокоактивный порошок для радиационных гамма-стерилизаторов и радиотерапии.");

        addItem("sr90_chloride",
                "Strontium-90 Chloride (90SrCl2)\nFormula: 90SrCl2 (Molar mass: 160.81 g/mol)\nComposition: 55.91% 90Sr, 44.09% Cl\nSoluble pure beta-emitter salt exhibiting deadly bone-seeking radiotoxicity.",
                "Хлорид стронция-90 (90SrCl2)\nФормула: 90SrCl2 (Молярная масса: 160.81 г/моль)\nСостав: 55.91% 90Sr, 44.09% Cl\nРастворимый чистый бета-излучатель деления с высокой тропностью к костной ткани.");

        addItem("sr90_titanate",
                "Strontium-90 Titanate (90SrTiO3)\nFormula: 90SrTiO3 (Molar mass: 185.78 g/mol)\nComposition: 48.40% 90Sr, 25.81% O, 25.79% Ti\nChemically inert heat-producing ceramic fuel powering terrestrial nuclear RTGs.",
                "Титанат стронция-90 (90SrTiO3)\nФормула: 90SrTiO3 (Молярная масса: 185.78 г/моль)\nСостав: 48.40% 90Sr, 25.81% O, 25.79% Ti\nХимически инертная термостойкая керамика, топливо советских маячных РИТЭГ.");

        addItem("co60_pellet",
                "Cobalt-60 Industrial Pellet (60Co)\nFormula: 60Co (Molar mass: 59.93 g/mol)\nComposition: 100% 60Co\nNickel-plated metallic cylinder delivering lethal mega-curie gamma defectoscopy fields.",
                "Кобальт-60 (Промышленный источник / 60Co)\nФормула: 60Co (Молярная масса: 59.93 г/моль)\nСостав: 100% 60Co\nНикелированная гранула жесткого каскадного гамма-излучения для дефектоскопии.");
    }

    public static DeferredItem<Item> addItem(String name) {
        return ItemRegistryApi.addItem(name);
    }

    public static DeferredItem<Item> addItem(String name, String enDesc, String ruDesc) {
        return ItemRegistryApi.addItem(name, enDesc, ruDesc);
    }

    public static Item get(String name) {
        if ("dosimeter".equals(name)) {
            return DOSIMETER != null ? DOSIMETER.get() : null;
        }
        return ItemRegistryApi.getItem(name);
    }

    public static DeferredItem<Item> getHolder(String name) {
        if ("dosimeter".equals(name)) {
            return DOSIMETER;
        }
        return ItemRegistryApi.getHolder(name);
    }

    public static ItemStack stack(String name) {
        return ItemRegistryApi.getStack(name, 1);
    }

    public static ItemStack stack(String name, int count) {
        return ItemRegistryApi.getStack(name, count);
    }

    public static void register(IEventBus eventBus) {
        init();
        ItemRegistryApi.register(eventBus);
    }
}