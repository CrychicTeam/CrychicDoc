package com.yungnickyoung.minecraft.yungsapi.noise;

import com.yungnickyoung.minecraft.yungsapi.math.Vector2f;
import com.yungnickyoung.minecraft.yungsapi.math.Vector3f;

public class FastNoise implements INoiseLibrary {

    private int m_seed = 1337;

    private float m_frequency = 0.01F;

    private FastNoise.Interp m_interp = FastNoise.Interp.Quintic;

    private FastNoise.NoiseType m_noiseType = FastNoise.NoiseType.Simplex;

    private int m_octaves = 3;

    private float m_lacunarity = 2.0F;

    private float m_gain = 0.5F;

    private FastNoise.FractalType m_fractalType = FastNoise.FractalType.FBM;

    private float m_fractalBounding;

    private FastNoise.CellularDistanceFunction m_cellularDistanceFunction = FastNoise.CellularDistanceFunction.Euclidean;

    private FastNoise.CellularReturnType m_cellularReturnType = FastNoise.CellularReturnType.CellValue;

    private FastNoise m_cellularNoiseLookup = null;

    private float m_gradientPerturbAmp = 2.2222223F;

    private static final FastNoise.Float2[] GRAD_2D = new FastNoise.Float2[] { new FastNoise.Float2(-1.0F, -1.0F), new FastNoise.Float2(1.0F, -1.0F), new FastNoise.Float2(-1.0F, 1.0F), new FastNoise.Float2(1.0F, 1.0F), new FastNoise.Float2(0.0F, -1.0F), new FastNoise.Float2(-1.0F, 0.0F), new FastNoise.Float2(0.0F, 1.0F), new FastNoise.Float2(1.0F, 0.0F) };

    private static final FastNoise.Float3[] GRAD_3D = new FastNoise.Float3[] { new FastNoise.Float3(1.0F, 1.0F, 0.0F), new FastNoise.Float3(-1.0F, 1.0F, 0.0F), new FastNoise.Float3(1.0F, -1.0F, 0.0F), new FastNoise.Float3(-1.0F, -1.0F, 0.0F), new FastNoise.Float3(1.0F, 0.0F, 1.0F), new FastNoise.Float3(-1.0F, 0.0F, 1.0F), new FastNoise.Float3(1.0F, 0.0F, -1.0F), new FastNoise.Float3(-1.0F, 0.0F, -1.0F), new FastNoise.Float3(0.0F, 1.0F, 1.0F), new FastNoise.Float3(0.0F, -1.0F, 1.0F), new FastNoise.Float3(0.0F, 1.0F, -1.0F), new FastNoise.Float3(0.0F, -1.0F, -1.0F), new FastNoise.Float3(1.0F, 1.0F, 0.0F), new FastNoise.Float3(0.0F, -1.0F, 1.0F), new FastNoise.Float3(-1.0F, 1.0F, 0.0F), new FastNoise.Float3(0.0F, -1.0F, -1.0F) };

    private static final FastNoise.Float2[] CELL_2D = new FastNoise.Float2[] { new FastNoise.Float2(-0.43135393F, 0.12819435F), new FastNoise.Float2(-0.17333168F, 0.41527838F), new FastNoise.Float2(-0.28219575F, -0.35052183F), new FastNoise.Float2(-0.28064737F, 0.35176277F), new FastNoise.Float2(0.3125509F, -0.3237467F), new FastNoise.Float2(0.33830184F, -0.29673535F), new FastNoise.Float2(-0.4393982F, -0.09710417F), new FastNoise.Float2(-0.44604436F, -0.05953503F), new FastNoise.Float2(-0.30222303F, 0.3334085F), new FastNoise.Float2(-0.21268106F, -0.39656875F), new FastNoise.Float2(-0.29911566F, 0.33619907F), new FastNoise.Float2(0.22933237F, 0.38717782F), new FastNoise.Float2(0.44754392F, -0.046951506F), new FastNoise.Float2(0.1777518F, 0.41340572F), new FastNoise.Float2(0.16885225F, -0.4171198F), new FastNoise.Float2(-0.097659715F, 0.43927506F), new FastNoise.Float2(0.084501885F, 0.44199485F), new FastNoise.Float2(-0.40987605F, -0.18574613F), new FastNoise.Float2(0.34765857F, -0.2857158F), new FastNoise.Float2(-0.335067F, -0.30038327F), new FastNoise.Float2(0.229819F, -0.38688916F), new FastNoise.Float2(-0.010699241F, 0.4498728F), new FastNoise.Float2(-0.44601414F, -0.059761196F), new FastNoise.Float2(0.3650294F, 0.26316068F), new FastNoise.Float2(-0.34947944F, 0.28348568F), new FastNoise.Float2(-0.41227207F, 0.18036559F), new FastNoise.Float2(-0.26732782F, 0.36198872F), new FastNoise.Float2(0.32212403F, -0.31422302F), new FastNoise.Float2(0.2880446F, -0.34573156F), new FastNoise.Float2(0.38921708F, -0.22585405F), new FastNoise.Float2(0.4492085F, -0.026678115F), new FastNoise.Float2(-0.44977248F, 0.014307996F), new FastNoise.Float2(0.12781754F, -0.43146574F), new FastNoise.Float2(-0.035721004F, 0.44858F), new FastNoise.Float2(-0.4297407F, -0.13350253F), new FastNoise.Float2(-0.32178178F, 0.3145735F), new FastNoise.Float2(-0.3057159F, 0.33020872F), new FastNoise.Float2(-0.414504F, 0.17517549F), new FastNoise.Float2(-0.373814F, 0.25052565F), new FastNoise.Float2(0.22368914F, -0.39046532F), new FastNoise.Float2(0.0029677756F, -0.4499902F), new FastNoise.Float2(0.17471284F, -0.4146992F), new FastNoise.Float2(-0.44237724F, -0.08247648F), new FastNoise.Float2(-0.2763961F, -0.35511294F), new FastNoise.Float2(-0.4019386F, -0.20234962F), new FastNoise.Float2(0.3871414F, -0.22939382F), new FastNoise.Float2(-0.43000874F, 0.1326367F), new FastNoise.Float2(-0.030375743F, -0.44897363F), new FastNoise.Float2(-0.34861815F, 0.28454417F), new FastNoise.Float2(0.045535173F, -0.44769025F), new FastNoise.Float2(-0.037580293F, 0.44842806F), new FastNoise.Float2(0.3266409F, 0.309525F), new FastNoise.Float2(0.065400176F, -0.4452222F), new FastNoise.Float2(0.03409026F, 0.44870687F), new FastNoise.Float2(-0.44491938F, 0.06742967F), new FastNoise.Float2(-0.4255936F, -0.14618507F), new FastNoise.Float2(0.4499173F, 0.008627303F), new FastNoise.Float2(0.052426063F, 0.44693568F), new FastNoise.Float2(-0.4495305F, -0.020550266F), new FastNoise.Float2(-0.12047757F, 0.43357256F), new FastNoise.Float2(-0.3419864F, -0.2924813F), new FastNoise.Float2(0.386532F, 0.23041917F), new FastNoise.Float2(0.045060977F, -0.4477382F), new FastNoise.Float2(-0.06283466F, 0.4455915F), new FastNoise.Float2(0.39326003F, -0.21873853F), new FastNoise.Float2(0.44722617F, -0.04988731F), new FastNoise.Float2(0.3753571F, -0.24820767F), new FastNoise.Float2(-0.2736623F, 0.35722396F), new FastNoise.Float2(0.17004615F, 0.4166345F), new FastNoise.Float2(0.41026923F, 0.18487608F), new FastNoise.Float2(0.3232272F, -0.31308815F), new FastNoise.Float2(-0.28823102F, -0.34557614F), new FastNoise.Float2(0.20509727F, 0.4005435F), new FastNoise.Float2(0.4414086F, -0.08751257F), new FastNoise.Float2(-0.16847004F, 0.4172743F), new FastNoise.Float2(-0.0039780326F, 0.4499824F), new FastNoise.Float2(-0.20551336F, 0.4003302F), new FastNoise.Float2(-0.006095675F, -0.4499587F), new FastNoise.Float2(-0.11962281F, -0.43380916F), new FastNoise.Float2(0.39015284F, -0.2242337F), new FastNoise.Float2(0.017235318F, 0.4496698F), new FastNoise.Float2(-0.30150703F, 0.33405614F), new FastNoise.Float2(-0.015142624F, -0.44974515F), new FastNoise.Float2(-0.4142574F, -0.1757578F), new FastNoise.Float2(-0.19163772F, -0.40715474F), new FastNoise.Float2(0.37492487F, 0.24886008F), new FastNoise.Float2(-0.22377743F, 0.39041474F), new FastNoise.Float2(-0.41663432F, -0.17004661F), new FastNoise.Float2(0.36191717F, 0.2674247F), new FastNoise.Float2(0.18911268F, -0.4083337F), new FastNoise.Float2(-0.3127425F, 0.3235616F), new FastNoise.Float2(-0.3281808F, 0.30789182F), new FastNoise.Float2(-0.22948067F, 0.38708994F), new FastNoise.Float2(-0.34452662F, 0.28948474F), new FastNoise.Float2(-0.41670954F, -0.16986217F), new FastNoise.Float2(-0.2578903F, -0.36877173F), new FastNoise.Float2(-0.3612038F, 0.26838747F), new FastNoise.Float2(0.22679965F, 0.38866684F), new FastNoise.Float2(0.20715706F, 0.3994821F), new FastNoise.Float2(0.083551764F, -0.44217542F), new FastNoise.Float2(-0.43122333F, 0.12863296F), new FastNoise.Float2(0.32570556F, 0.3105091F), new FastNoise.Float2(0.1777011F, -0.41342753F), new FastNoise.Float2(-0.44518253F, 0.0656698F), new FastNoise.Float2(0.39551434F, 0.21463552F), new FastNoise.Float2(-0.4264614F, 0.14363383F), new FastNoise.Float2(-0.37937996F, -0.24201414F), new FastNoise.Float2(0.04617599F, -0.4476246F), new FastNoise.Float2(-0.37140542F, -0.25408268F), new FastNoise.Float2(0.25635704F, -0.36983925F), new FastNoise.Float2(0.03476646F, 0.44865498F), new FastNoise.Float2(-0.30654544F, 0.32943875F), new FastNoise.Float2(-0.22569798F, 0.38930762F), new FastNoise.Float2(0.41164485F, -0.18179253F), new FastNoise.Float2(-0.29077458F, -0.3434387F), new FastNoise.Float2(0.28422785F, -0.3488761F), new FastNoise.Float2(0.31145895F, -0.32479736F), new FastNoise.Float2(0.44641557F, -0.05668443F), new FastNoise.Float2(-0.3037334F, -0.33203316F), new FastNoise.Float2(0.4079607F, 0.18991591F), new FastNoise.Float2(-0.3486949F, -0.2844501F), new FastNoise.Float2(0.32648215F, 0.30969244F), new FastNoise.Float2(0.32111424F, 0.3152549F), new FastNoise.Float2(0.011833827F, 0.44984436F), new FastNoise.Float2(0.43338442F, 0.1211526F), new FastNoise.Float2(0.31186685F, 0.32440573F), new FastNoise.Float2(-0.27275348F, 0.35791835F), new FastNoise.Float2(-0.42222863F, -0.15563737F), new FastNoise.Float2(-0.10097001F, -0.438526F), new FastNoise.Float2(-0.2741171F, -0.35687506F), new FastNoise.Float2(-0.14651251F, 0.425481F), new FastNoise.Float2(0.2302279F, -0.38664597F), new FastNoise.Float2(-0.36994356F, 0.25620648F), new FastNoise.Float2(0.10570035F, -0.4374099F), new FastNoise.Float2(-0.26467136F, 0.36393553F), new FastNoise.Float2(0.3521828F, 0.2801201F), new FastNoise.Float2(-0.18641879F, -0.40957054F), new FastNoise.Float2(0.1994493F, -0.40338564F), new FastNoise.Float2(0.3937065F, 0.21793391F), new FastNoise.Float2(-0.32261583F, 0.31371805F), new FastNoise.Float2(0.37962353F, 0.2416319F), new FastNoise.Float2(0.1482922F, 0.424864F), new FastNoise.Float2(-0.4074004F, 0.19111493F), new FastNoise.Float2(0.4212853F, 0.15817298F), new FastNoise.Float2(-0.26212972F, 0.36577043F), new FastNoise.Float2(-0.2536987F, -0.37166783F), new FastNoise.Float2(-0.21002364F, 0.3979825F), new FastNoise.Float2(0.36241525F, 0.2667493F), new FastNoise.Float2(-0.36450386F, -0.26388812F), new FastNoise.Float2(0.23184867F, 0.38567626F), new FastNoise.Float2(-0.3260457F, 0.3101519F), new FastNoise.Float2(-0.21300453F, -0.3963951F), new FastNoise.Float2(0.3814999F, -0.23865843F), new FastNoise.Float2(-0.34297732F, 0.29131868F), new FastNoise.Float2(-0.43558657F, 0.11297941F), new FastNoise.Float2(-0.21046796F, 0.3977477F), new FastNoise.Float2(0.33483645F, -0.30064023F), new FastNoise.Float2(0.34304687F, 0.29123673F), new FastNoise.Float2(-0.22918367F, -0.38726586F), new FastNoise.Float2(0.25477073F, -0.3709338F), new FastNoise.Float2(0.42361748F, -0.1518164F), new FastNoise.Float2(-0.15387742F, 0.4228732F), new FastNoise.Float2(-0.44074494F, 0.09079596F), new FastNoise.Float2(-0.06805276F, -0.4448245F), new FastNoise.Float2(0.44535172F, -0.06451237F), new FastNoise.Float2(0.25624645F, -0.36991587F), new FastNoise.Float2(0.32781982F, -0.30827612F), new FastNoise.Float2(-0.41227743F, -0.18035334F), new FastNoise.Float2(0.3354091F, -0.30000123F), new FastNoise.Float2(0.44663286F, -0.054946158F), new FastNoise.Float2(-0.16089533F, 0.42025313F), new FastNoise.Float2(-0.09463955F, 0.43993562F), new FastNoise.Float2(-0.026376883F, -0.4492263F), new FastNoise.Float2(0.44710281F, -0.050981198F), new FastNoise.Float2(-0.4365671F, 0.10912917F), new FastNoise.Float2(-0.39598587F, 0.21376434F), new FastNoise.Float2(-0.42400482F, -0.15073125F), new FastNoise.Float2(-0.38827947F, 0.22746222F), new FastNoise.Float2(-0.42836526F, -0.13785212F), new FastNoise.Float2(0.3303888F, 0.30552125F), new FastNoise.Float2(0.3321435F, -0.30361274F), new FastNoise.Float2(-0.41302106F, -0.17864382F), new FastNoise.Float2(0.084030606F, -0.44208467F), new FastNoise.Float2(-0.38228828F, 0.23739347F), new FastNoise.Float2(-0.37123957F, -0.25432497F), new FastNoise.Float2(0.4472364F, -0.049795635F), new FastNoise.Float2(-0.44665912F, 0.054732345F), new FastNoise.Float2(0.048627254F, -0.44736493F), new FastNoise.Float2(-0.42031014F, -0.16074637F), new FastNoise.Float2(0.22053608F, 0.3922548F), new FastNoise.Float2(-0.36249006F, 0.2666476F), new FastNoise.Float2(-0.40360868F, -0.19899757F), new FastNoise.Float2(0.21527278F, 0.39516786F), new FastNoise.Float2(-0.43593928F, -0.11161062F), new FastNoise.Float2(0.4178354F, 0.1670735F), new FastNoise.Float2(0.20076302F, 0.40273342F), new FastNoise.Float2(-0.07278067F, -0.4440754F), new FastNoise.Float2(0.36447486F, -0.26392817F), new FastNoise.Float2(-0.43174517F, 0.12687041F), new FastNoise.Float2(-0.29743645F, 0.33768559F), new FastNoise.Float2(-0.2998672F, 0.3355289F), new FastNoise.Float2(-0.26736742F, 0.3619595F), new FastNoise.Float2(0.28084233F, 0.35160714F), new FastNoise.Float2(0.34989464F, 0.28297302F), new FastNoise.Float2(-0.22296856F, 0.39087725F), new FastNoise.Float2(0.33058232F, 0.30531186F), new FastNoise.Float2(-0.24366812F, -0.37831977F), new FastNoise.Float2(-0.034027766F, 0.4487116F), new FastNoise.Float2(-0.31935883F, 0.31703302F), new FastNoise.Float2(0.44546336F, -0.063737005F), new FastNoise.Float2(0.44835043F, 0.03849544F), new FastNoise.Float2(-0.44273585F, -0.08052933F), new FastNoise.Float2(0.054522987F, 0.44668472F), new FastNoise.Float2(-0.28125608F, 0.35127628F), new FastNoise.Float2(0.12666969F, 0.43180412F), new FastNoise.Float2(-0.37359813F, 0.25084746F), new FastNoise.Float2(0.29597083F, -0.3389709F), new FastNoise.Float2(-0.37143773F, 0.25403547F), new FastNoise.Float2(-0.4044671F, -0.19724695F), new FastNoise.Float2(0.16361657F, -0.41920117F), new FastNoise.Float2(0.32891855F, -0.30710354F), new FastNoise.Float2(-0.2494825F, -0.374511F), new FastNoise.Float2(0.032831334F, 0.44880074F), new FastNoise.Float2(-0.16630606F, -0.41814148F), new FastNoise.Float2(-0.10683318F, 0.43713462F), new FastNoise.Float2(0.0644026F, -0.4453676F), new FastNoise.Float2(-0.4483231F, 0.03881238F), new FastNoise.Float2(-0.42137775F, -0.15792651F), new FastNoise.Float2(0.05097921F, -0.44710302F), new FastNoise.Float2(0.20505841F, -0.40056342F), new FastNoise.Float2(0.41780984F, -0.16713744F), new FastNoise.Float2(-0.35651895F, -0.27458012F), new FastNoise.Float2(0.44783983F, 0.04403978F), new FastNoise.Float2(-0.33999997F, -0.2947881F), new FastNoise.Float2(0.3767122F, 0.24614613F), new FastNoise.Float2(-0.31389344F, 0.32244518F), new FastNoise.Float2(-0.14620018F, -0.42558843F), new FastNoise.Float2(0.39702904F, -0.21182053F), new FastNoise.Float2(0.44591492F, -0.0604969F), new FastNoise.Float2(-0.41048893F, -0.18438771F), new FastNoise.Float2(0.1475104F, -0.4251361F), new FastNoise.Float2(0.0925803F, 0.44037357F), new FastNoise.Float2(-0.15896647F, -0.42098653F), new FastNoise.Float2(0.2482445F, 0.37533274F), new FastNoise.Float2(0.43836242F, -0.10167786F), new FastNoise.Float2(0.06242803F, 0.44564867F), new FastNoise.Float2(0.2846591F, -0.3485243F), new FastNoise.Float2(-0.34420276F, -0.28986976F), new FastNoise.Float2(0.11981889F, -0.43375504F), new FastNoise.Float2(-0.2435907F, 0.37836963F), new FastNoise.Float2(0.2958191F, -0.3391033F), new FastNoise.Float2(-0.1164008F, 0.43468478F), new FastNoise.Float2(0.12740372F, -0.4315881F), new FastNoise.Float2(0.3680473F, 0.2589231F), new FastNoise.Float2(0.2451437F, 0.3773653F), new FastNoise.Float2(-0.43145096F, 0.12786736F) };

    private static final FastNoise.Float3[] CELL_3D = new FastNoise.Float3[] { new FastNoise.Float3(0.14537874F, -0.41497818F, -0.09569818F), new FastNoise.Float3(-0.012428297F, -0.14579184F, -0.42554703F), new FastNoise.Float3(0.28779796F, -0.026064834F, -0.34495357F), new FastNoise.Float3(-0.07732987F, 0.23770943F, 0.37418488F), new FastNoise.Float3(0.11072059F, -0.3552302F, -0.25308585F), new FastNoise.Float3(0.27552092F, 0.26405212F, -0.23846321F), new FastNoise.Float3(0.29416895F, 0.15260646F, 0.30442718F), new FastNoise.Float3(0.4000921F, -0.20340563F, 0.0324415F), new FastNoise.Float3(-0.16973041F, 0.39708647F, -0.12654613F), new FastNoise.Float3(-0.14832245F, -0.38596946F, 0.17756131F), new FastNoise.Float3(0.2623597F, -0.2354853F, 0.27966776F), new FastNoise.Float3(-0.2709003F, 0.3505271F, -0.07901747F), new FastNoise.Float3(-0.035165507F, 0.38852343F, 0.22430544F), new FastNoise.Float3(-0.12677127F, 0.1920044F, 0.38673422F), new FastNoise.Float3(0.02952022F, 0.44096857F, 0.084706925F), new FastNoise.Float3(-0.28068542F, -0.26699677F, 0.22897254F), new FastNoise.Float3(-0.17115955F, 0.21411856F, 0.35687205F), new FastNoise.Float3(0.21132272F, 0.39024058F, -0.074531786F), new FastNoise.Float3(-0.10243528F, 0.21280442F, -0.38304216F), new FastNoise.Float3(-0.330425F, -0.15669867F, 0.26223055F), new FastNoise.Float3(0.20911114F, 0.31332782F, -0.24616706F), new FastNoise.Float3(0.34467816F, -0.19442405F, -0.21423413F), new FastNoise.Float3(0.19844781F, -0.32143423F, -0.24453732F), new FastNoise.Float3(-0.29290086F, 0.22629151F, 0.2559321F), new FastNoise.Float3(-0.16173328F, 0.00631477F, -0.41988388F), new FastNoise.Float3(-0.35820603F, -0.14830318F, -0.2284614F), new FastNoise.Float3(-0.18520673F, -0.34541193F, -0.2211087F), new FastNoise.Float3(0.3046301F, 0.10263104F, 0.3149085F), new FastNoise.Float3(-0.038167685F, -0.25517663F, -0.3686843F), new FastNoise.Float3(-0.40849522F, 0.18059509F, 0.05492789F), new FastNoise.Float3(-0.026874434F, -0.27497414F, 0.35519993F), new FastNoise.Float3(-0.038010985F, 0.3277859F, 0.30596006F), new FastNoise.Float3(0.23711208F, 0.29003868F, -0.2493099F), new FastNoise.Float3(0.44476604F, 0.039469305F, 0.05590469F), new FastNoise.Float3(0.019851472F, -0.015031833F, -0.44931054F), new FastNoise.Float3(0.4274339F, 0.033459943F, -0.1366773F), new FastNoise.Float3(-0.20729886F, 0.28714147F, -0.27762738F), new FastNoise.Float3(-0.3791241F, 0.12811777F, 0.205793F), new FastNoise.Float3(-0.20987213F, -0.10070873F, -0.38511226F), new FastNoise.Float3(0.01582799F, 0.42638946F, 0.14297384F), new FastNoise.Float3(-0.18881294F, -0.31609967F, -0.2587096F), new FastNoise.Float3(0.1612989F, -0.19748051F, -0.3707885F), new FastNoise.Float3(-0.08974491F, 0.22914875F, -0.37674487F), new FastNoise.Float3(0.07041229F, 0.41502303F, -0.15905343F), new FastNoise.Float3(-0.108292565F, -0.15860616F, 0.40696046F), new FastNoise.Float3(0.24741006F, -0.33094147F, 0.17823021F), new FastNoise.Float3(-0.10688367F, -0.27016446F, -0.34363797F), new FastNoise.Float3(0.23964521F, 0.068036005F, -0.37475494F), new FastNoise.Float3(-0.30638862F, 0.25974283F, 0.2028785F), new FastNoise.Float3(0.15933429F, -0.311435F, -0.2830562F), new FastNoise.Float3(0.27096906F, 0.14126487F, -0.33033317F), new FastNoise.Float3(-0.15197805F, 0.3623355F, 0.2193528F), new FastNoise.Float3(0.16997737F, 0.3456013F, 0.232739F), new FastNoise.Float3(-0.19861557F, 0.38362765F, -0.12602258F), new FastNoise.Float3(-0.18874821F, -0.2050155F, -0.35333094F), new FastNoise.Float3(0.26591033F, 0.3015631F, -0.20211722F), new FastNoise.Float3(-0.08838976F, -0.42888197F, -0.1036702F), new FastNoise.Float3(-0.042018693F, 0.30995926F, 0.3235115F), new FastNoise.Float3(-0.32303345F, 0.20154992F, -0.23984788F), new FastNoise.Float3(0.2612721F, 0.27598545F, -0.24097495F), new FastNoise.Float3(0.38571304F, 0.21934603F, 0.074918374F), new FastNoise.Float3(0.07654968F, 0.37217322F, 0.24109592F), new FastNoise.Float3(0.4317039F, -0.02577753F, 0.12436751F), new FastNoise.Float3(-0.28904364F, -0.341818F, -0.045980845F), new FastNoise.Float3(-0.22019476F, 0.38302338F, -0.085483104F), new FastNoise.Float3(0.41613227F, -0.16696343F, -0.03817252F), new FastNoise.Float3(0.22047181F, 0.02654239F, -0.391392F), new FastNoise.Float3(-0.10403074F, 0.38900796F, -0.2008741F), new FastNoise.Float3(-0.14321226F, 0.3716144F, -0.20950656F), new FastNoise.Float3(0.39783806F, -0.062066693F, 0.20092937F), new FastNoise.Float3(-0.25992745F, 0.2616725F, -0.25780848F), new FastNoise.Float3(0.40326184F, -0.11245936F, 0.1650236F), new FastNoise.Float3(-0.0895347F, -0.30482447F, 0.31869355F), new FastNoise.Float3(0.1189372F, -0.2875222F, 0.3250922F), new FastNoise.Float3(0.02167047F, -0.032846306F, -0.44827616F), new FastNoise.Float3(-0.34113437F, 0.2500031F, 0.15370683F), new FastNoise.Float3(0.31629646F, 0.3082064F, -0.08640228F), new FastNoise.Float3(0.2355139F, -0.34393343F, -0.16953762F), new FastNoise.Float3(-0.028745415F, -0.39559332F, 0.21255504F), new FastNoise.Float3(-0.24614552F, 0.020202823F, -0.3761705F), new FastNoise.Float3(0.042080294F, -0.44704396F, 0.029680781F), new FastNoise.Float3(0.27274588F, 0.22884719F, -0.27520657F), new FastNoise.Float3(-0.13475229F, -0.027208483F, -0.42848748F), new FastNoise.Float3(0.38296244F, 0.123193145F, -0.20165123F), new FastNoise.Float3(-0.35476136F, 0.12717022F, 0.24591078F), new FastNoise.Float3(0.23057902F, 0.30638957F, 0.23549682F), new FastNoise.Float3(-0.08323845F, -0.19222452F, 0.39827263F), new FastNoise.Float3(0.2993663F, -0.2619918F, -0.21033332F), new FastNoise.Float3(-0.21548657F, 0.27067477F, 0.2877511F), new FastNoise.Float3(0.016833553F, -0.26806557F, -0.36105052F), new FastNoise.Float3(0.052404292F, 0.4335128F, -0.108721785F), new FastNoise.Float3(0.0094010485F, -0.44728905F, 0.0484161F), new FastNoise.Float3(0.34656888F, 0.011419145F, -0.28680938F), new FastNoise.Float3(-0.3706868F, -0.25511044F, 0.0031566927F), new FastNoise.Float3(0.274117F, 0.21399724F, -0.28559598F), new FastNoise.Float3(0.06413434F, 0.17087185F, 0.41132662F), new FastNoise.Float3(-0.38818797F, -0.039732803F, -0.22412363F), new FastNoise.Float3(0.064194694F, -0.28036824F, 0.3460819F), new FastNoise.Float3(-0.19861208F, -0.33911735F, 0.21920918F), new FastNoise.Float3(-0.20320301F, -0.38716415F, 0.10636004F), new FastNoise.Float3(-0.13897364F, -0.27759016F, -0.32577604F), new FastNoise.Float3(-0.065556414F, 0.34225327F, -0.28471926F), new FastNoise.Float3(-0.25292465F, -0.2904228F, 0.23277397F), new FastNoise.Float3(0.14444765F, 0.1069184F, 0.41255707F), new FastNoise.Float3(-0.364378F, -0.24471F, -0.09922543F), new FastNoise.Float3(0.42861426F, -0.13584961F, -0.018295068F), new FastNoise.Float3(0.16587292F, -0.31368086F, -0.27674988F), new FastNoise.Float3(0.22196105F, -0.365814F, 0.13933203F), new FastNoise.Float3(0.043229405F, -0.38327307F, 0.23180372F), new FastNoise.Float3(-0.0848127F, -0.44048697F, -0.035749655F), new FastNoise.Float3(0.18220821F, -0.39532593F, 0.1140946F), new FastNoise.Float3(-0.32693234F, 0.30365425F, 0.05838957F), new FastNoise.Float3(-0.40804854F, 0.042278584F, -0.18495652F), new FastNoise.Float3(0.26760253F, -0.012996716F, 0.36155218F), new FastNoise.Float3(0.30248925F, -0.10099903F, -0.3174893F), new FastNoise.Float3(0.1448494F, 0.42592168F, -0.01045808F), new FastNoise.Float3(0.41984022F, 0.0806232F, 0.14047809F), new FastNoise.Float3(-0.30088723F, -0.3330409F, -0.032413557F), new FastNoise.Float3(0.36393103F, -0.12912844F, -0.23104121F), new FastNoise.Float3(0.32958066F, 0.018417599F, -0.30583882F), new FastNoise.Float3(0.27762595F, -0.2974929F, -0.19215047F), new FastNoise.Float3(0.41490006F, -0.14479318F, -0.096916884F), new FastNoise.Float3(0.14501671F, -0.039899293F, 0.4241205F), new FastNoise.Float3(0.092990234F, -0.29973218F, -0.32251117F), new FastNoise.Float3(0.10289071F, -0.36126688F, 0.24778973F), new FastNoise.Float3(0.26830572F, -0.070760414F, -0.35426685F), new FastNoise.Float3(-0.4227307F, -0.07933162F, -0.13230732F), new FastNoise.Float3(-0.17812248F, 0.18068571F, -0.3716518F), new FastNoise.Float3(0.43907887F, -0.028418485F, -0.094351165F), new FastNoise.Float3(0.29725835F, 0.23827997F, -0.23949975F), new FastNoise.Float3(-0.17070028F, 0.22158457F, 0.3525077F), new FastNoise.Float3(0.38066867F, 0.14718525F, -0.18954648F), new FastNoise.Float3(-0.17514457F, -0.2748879F, 0.31025964F), new FastNoise.Float3(-0.22272375F, -0.23167789F, 0.31499124F), new FastNoise.Float3(0.13696331F, 0.13413431F, -0.40712288F), new FastNoise.Float3(-0.35295033F, -0.24728934F, -0.1295146F), new FastNoise.Float3(-0.25907442F, -0.29855776F, -0.21504351F), new FastNoise.Float3(-0.37840194F, 0.21998167F, -0.10449899F), new FastNoise.Float3(-0.056358058F, 0.14857374F, 0.42101023F), new FastNoise.Float3(0.32514286F, 0.09666047F, -0.29570064F), new FastNoise.Float3(-0.41909957F, 0.14067514F, -0.08405979F), new FastNoise.Float3(-0.3253151F, -0.3080335F, -0.042254567F), new FastNoise.Float3(0.2857946F, -0.05796152F, 0.34272718F), new FastNoise.Float3(-0.2733604F, 0.1973771F, -0.29802075F), new FastNoise.Float3(0.21900366F, 0.24100378F, -0.31057137F), new FastNoise.Float3(0.31827673F, -0.27134296F, 0.16605099F), new FastNoise.Float3(-0.03222023F, -0.33311614F, -0.30082467F), new FastNoise.Float3(-0.30877802F, 0.19927941F, -0.25969952F), new FastNoise.Float3(-0.06487612F, -0.4311323F, 0.11142734F), new FastNoise.Float3(0.39211714F, -0.06294284F, -0.2116184F), new FastNoise.Float3(-0.16064045F, -0.3589281F, -0.21878128F), new FastNoise.Float3(-0.037677713F, -0.22903514F, 0.3855169F), new FastNoise.Float3(0.13948669F, -0.3602214F, 0.23083329F), new FastNoise.Float3(-0.4345094F, 0.005751117F, 0.11691243F), new FastNoise.Float3(-0.10446375F, 0.41681284F, -0.13362028F), new FastNoise.Float3(0.26587275F, 0.25519434F, 0.2582393F), new FastNoise.Float3(0.2051462F, 0.19753908F, 0.3484155F), new FastNoise.Float3(-0.26608557F, 0.23483312F, 0.2766801F), new FastNoise.Float3(0.07849406F, -0.33003464F, -0.29566166F), new FastNoise.Float3(-0.21606864F, 0.053764515F, -0.39105463F), new FastNoise.Float3(-0.18577918F, 0.21484992F, 0.34903526F), new FastNoise.Float3(0.024924217F, -0.32299542F, -0.31233433F), new FastNoise.Float3(-0.12016783F, 0.40172666F, 0.16332598F), new FastNoise.Float3(-0.021600846F, -0.06885389F, 0.44417626F), new FastNoise.Float3(0.259767F, 0.30963007F, 0.19786438F), new FastNoise.Float3(-0.16115539F, -0.09823036F, 0.40850917F), new FastNoise.Float3(-0.32788968F, 0.14616702F, 0.27133662F), new FastNoise.Float3(0.2822735F, 0.03754421F, -0.3484424F), new FastNoise.Float3(0.03169341F, 0.34740525F, -0.28426242F), new FastNoise.Float3(0.22026137F, -0.3460788F, -0.18497133F), new FastNoise.Float3(0.2933396F, 0.30319735F, 0.15659896F), new FastNoise.Float3(-0.3194923F, 0.24537522F, -0.20053846F), new FastNoise.Float3(-0.3441586F, -0.16988562F, -0.23493347F), new FastNoise.Float3(0.27036458F, -0.35742772F, 0.040600598F), new FastNoise.Float3(0.2298569F, 0.37441564F, 0.09735889F), new FastNoise.Float3(0.09326604F, -0.31701088F, 0.30545956F), new FastNoise.Float3(-0.11161653F, -0.29850188F, 0.31770802F), new FastNoise.Float3(0.21729073F, -0.34600052F, -0.1885958F), new FastNoise.Float3(0.19913395F, 0.38203415F, -0.12998295F), new FastNoise.Float3(-0.054191817F, -0.21031451F, 0.3941206F), new FastNoise.Float3(0.08871337F, 0.20121174F, 0.39261147F), new FastNoise.Float3(0.27876732F, 0.35054046F, 0.04370535F), new FastNoise.Float3(-0.32216644F, 0.30672136F, 0.06804997F), new FastNoise.Float3(-0.42773664F, 0.13206677F, 0.045822866F), new FastNoise.Float3(0.24013188F, -0.1612516F, 0.34472394F), new FastNoise.Float3(0.1448608F, -0.2387819F, 0.35284352F), new FastNoise.Float3(-0.38370657F, -0.22063984F, 0.081162356F), new FastNoise.Float3(-0.4382628F, -0.09082753F, -0.046648555F), new FastNoise.Float3(-0.37728354F, 0.05445141F, 0.23914887F), new FastNoise.Float3(0.12595794F, 0.34839457F, 0.25545222F), new FastNoise.Float3(-0.14062855F, -0.27087736F, -0.33067968F), new FastNoise.Float3(-0.15806945F, 0.4162932F, -0.06491554F), new FastNoise.Float3(0.2477612F, -0.29278675F, -0.23535146F), new FastNoise.Float3(0.29161328F, 0.33125353F, 0.08793625F), new FastNoise.Float3(0.073652655F, -0.16661598F, 0.4114783F), new FastNoise.Float3(-0.26126525F, -0.24222377F, 0.27489653F), new FastNoise.Float3(-0.3721862F, 0.25279015F, 0.008634938F), new FastNoise.Float3(-0.36911917F, -0.25528118F, 0.032902323F), new FastNoise.Float3(0.22784418F, -0.3358365F, 0.1944245F), new FastNoise.Float3(0.36339816F, -0.23101902F, 0.13065979F), new FastNoise.Float3(-0.3042315F, -0.26984522F, 0.19268309F), new FastNoise.Float3(-0.3199312F, 0.31633255F, -0.008816978F), new FastNoise.Float3(0.28748524F, 0.16422755F, -0.30476475F), new FastNoise.Float3(-0.14510968F, 0.3277541F, -0.27206695F), new FastNoise.Float3(0.3220091F, 0.05113441F, 0.31015387F), new FastNoise.Float3(-0.12474009F, -0.043336052F, -0.4301882F), new FastNoise.Float3(-0.2829556F, -0.30561906F, -0.1703911F), new FastNoise.Float3(0.10693844F, 0.34910247F, -0.26304305F), new FastNoise.Float3(-0.14206612F, -0.30553767F, -0.29826826F), new FastNoise.Float3(-0.25054833F, 0.31564668F, -0.20023163F), new FastNoise.Float3(0.3265788F, 0.18712291F, 0.24664004F), new FastNoise.Float3(0.07646097F, -0.30266908F, 0.3241067F), new FastNoise.Float3(0.34517714F, 0.27571207F, -0.085648015F), new FastNoise.Float3(0.29813796F, 0.2852657F, 0.17954728F), new FastNoise.Float3(0.28122503F, 0.34667164F, 0.056844097F), new FastNoise.Float3(0.43903455F, -0.0979043F, -0.012783354F), new FastNoise.Float3(0.21483733F, 0.18501726F, 0.3494475F), new FastNoise.Float3(0.2595421F, -0.07946825F, 0.3589188F), new FastNoise.Float3(0.3182823F, -0.30735552F, -0.08203022F), new FastNoise.Float3(-0.40898594F, -0.046477184F, 0.18185264F), new FastNoise.Float3(-0.2826749F, 0.07417482F, 0.34218854F), new FastNoise.Float3(0.34838647F, 0.22544225F, -0.1740766F), new FastNoise.Float3(-0.32264152F, -0.14205854F, -0.27968165F), new FastNoise.Float3(0.4330735F, -0.11886856F, -0.028594075F), new FastNoise.Float3(-0.08717822F, -0.39098963F, -0.20500502F), new FastNoise.Float3(-0.21496783F, 0.3939974F, -0.032478984F), new FastNoise.Float3(-0.26873308F, 0.32268628F, -0.16172849F), new FastNoise.Float3(0.2105665F, -0.1961317F, -0.34596834F), new FastNoise.Float3(0.43618459F, -0.11055175F, 0.0046166084F), new FastNoise.Float3(0.053333335F, -0.3136395F, -0.31825432F), new FastNoise.Float3(-0.059862167F, 0.13610291F, -0.4247264F), new FastNoise.Float3(0.36649886F, 0.2550543F, -0.055909745F), new FastNoise.Float3(-0.23410155F, -0.18240573F, 0.33826706F), new FastNoise.Float3(-0.047309477F, -0.422215F, -0.14831145F), new FastNoise.Float3(-0.23915662F, -0.25776964F, -0.28081828F), new FastNoise.Float3(-0.1242081F, 0.42569533F, -0.07652336F), new FastNoise.Float3(0.26148328F, -0.36501792F, 0.02980623F), new FastNoise.Float3(-0.27287948F, -0.3499629F, 0.07458405F), new FastNoise.Float3(0.0078929F, -0.16727713F, 0.41767937F), new FastNoise.Float3(-0.017303303F, 0.29784867F, -0.33687797F), new FastNoise.Float3(0.20548357F, -0.32526004F, -0.23341466F), new FastNoise.Float3(-0.3231995F, 0.15642828F, -0.2712421F), new FastNoise.Float3(-0.2669546F, 0.25993437F, -0.2523279F), new FastNoise.Float3(-0.05554373F, 0.3170814F, -0.3144428F), new FastNoise.Float3(-0.20839357F, -0.31092283F, -0.24979813F), new FastNoise.Float3(0.06989323F, -0.31561416F, 0.31305373F), new FastNoise.Float3(0.38475662F, -0.16053091F, -0.16938764F), new FastNoise.Float3(-0.30262154F, -0.30015376F, -0.14431883F), new FastNoise.Float3(0.34507355F, 0.0861152F, 0.27569625F), new FastNoise.Float3(0.18144733F, -0.27887824F, -0.3029914F), new FastNoise.Float3(-0.038550105F, 0.09795111F, 0.4375151F), new FastNoise.Float3(0.35336703F, 0.26657528F, 0.08105161F), new FastNoise.Float3(-0.007945601F, 0.14035943F, -0.42747644F), new FastNoise.Float3(0.40630993F, -0.14917682F, -0.123119935F), new FastNoise.Float3(-0.20167735F, 0.008816271F, -0.40217972F), new FastNoise.Float3(-0.075270556F, -0.42564347F, -0.12514779F) };

    private static final int X_PRIME = 1619;

    private static final int Y_PRIME = 31337;

    private static final int Z_PRIME = 6971;

    private static final int W_PRIME = 1013;

    private static final float F3 = 0.33333334F;

    private static final float G3 = 0.16666667F;

    private static final float G33 = -0.5F;

    private static final float F2 = 0.5F;

    private static final float G2 = 0.25F;

    private static final byte[] SIMPLEX_4D = new byte[] { 0, 1, 2, 3, 0, 1, 3, 2, 0, 0, 0, 0, 0, 2, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 0, 0, 2, 1, 3, 0, 0, 0, 0, 0, 3, 1, 2, 0, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 3, 0, 0, 0, 0, 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 0, 1, 2, 3, 1, 0, 1, 0, 2, 3, 1, 0, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 1, 0, 0, 0, 0, 2, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 1, 2, 3, 0, 2, 1, 0, 0, 0, 0, 3, 1, 2, 0, 2, 1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 0, 2, 0, 0, 0, 0, 3, 2, 0, 1, 3, 2, 1, 0 };

    private static final float F4 = 0.309017F;

    private static final float G4 = 0.1381966F;

    private static final float CUBIC_3D_BOUNDING = 0.2962963F;

    private static final float CUBIC_2D_BOUNDING = 0.44444445F;

    public FastNoise() {
        this(1337);
    }

    public FastNoise(int seed) {
        this.m_seed = seed;
        this.CalculateFractalBounding();
    }

    public static float GetDecimalType() {
        return 0.0F;
    }

    public int GetSeed() {
        return this.m_seed;
    }

    public void SetSeed(int seed) {
        this.m_seed = seed;
    }

    public void SetFrequency(float frequency) {
        this.m_frequency = frequency;
    }

    public void SetInterp(FastNoise.Interp interp) {
        this.m_interp = interp;
    }

    public void SetNoiseType(FastNoise.NoiseType noiseType) {
        this.m_noiseType = noiseType;
    }

    public void SetFractalOctaves(int octaves) {
        this.m_octaves = octaves;
        this.CalculateFractalBounding();
    }

    public void SetFractalLacunarity(float lacunarity) {
        this.m_lacunarity = lacunarity;
    }

    public void SetFractalGain(float gain) {
        this.m_gain = gain;
        this.CalculateFractalBounding();
    }

    public void SetFractalType(FastNoise.FractalType fractalType) {
        this.m_fractalType = fractalType;
    }

    public void SetCellularDistanceFunction(FastNoise.CellularDistanceFunction cellularDistanceFunction) {
        this.m_cellularDistanceFunction = cellularDistanceFunction;
    }

    public void SetCellularReturnType(FastNoise.CellularReturnType cellularReturnType) {
        this.m_cellularReturnType = cellularReturnType;
    }

    public void SetCellularNoiseLookup(FastNoise noise) {
        this.m_cellularNoiseLookup = noise;
    }

    public void SetGradientPerturbAmp(float gradientPerturbAmp) {
        this.m_gradientPerturbAmp = gradientPerturbAmp / 0.45F;
    }

    private static int FastFloor(float f) {
        return f >= 0.0F ? (int) f : (int) f - 1;
    }

    private static int FastRound(float f) {
        return f >= 0.0F ? (int) (f + 0.5F) : (int) (f - 0.5F);
    }

    private static float Lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    private static float InterpHermiteFunc(float t) {
        return t * t * (3.0F - 2.0F * t);
    }

    private static float InterpQuinticFunc(float t) {
        return t * t * t * (t * (t * 6.0F - 15.0F) + 10.0F);
    }

    private static float CubicLerp(float a, float b, float c, float d, float t) {
        float p = d - c - (a - b);
        return t * t * t * p + t * t * (a - b - p) + t * (c - a) + b;
    }

    private void CalculateFractalBounding() {
        float amp = this.m_gain;
        float ampFractal = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            ampFractal += amp;
            amp *= this.m_gain;
        }
        this.m_fractalBounding = 1.0F / ampFractal;
    }

    private static int Hash2D(int seed, int x, int y) {
        int hash = seed ^ 1619 * x;
        hash ^= 31337 * y;
        hash = hash * hash * hash * 60493;
        return hash >> 13 ^ hash;
    }

    private static int Hash3D(int seed, int x, int y, int z) {
        int hash = seed ^ 1619 * x;
        hash ^= 31337 * y;
        hash ^= 6971 * z;
        hash = hash * hash * hash * 60493;
        return hash >> 13 ^ hash;
    }

    private static int Hash4D(int seed, int x, int y, int z, int w) {
        int hash = seed ^ 1619 * x;
        hash ^= 31337 * y;
        hash ^= 6971 * z;
        hash ^= 1013 * w;
        hash = hash * hash * hash * 60493;
        return hash >> 13 ^ hash;
    }

    private static float ValCoord2D(int seed, int x, int y) {
        int n = seed ^ 1619 * x;
        n ^= 31337 * y;
        return (float) (n * n * n * 60493) / 2.1474836E9F;
    }

    private static float ValCoord3D(int seed, int x, int y, int z) {
        int n = seed ^ 1619 * x;
        n ^= 31337 * y;
        n ^= 6971 * z;
        return (float) (n * n * n * 60493) / 2.1474836E9F;
    }

    private static float ValCoord4D(int seed, int x, int y, int z, int w) {
        int n = seed ^ 1619 * x;
        n ^= 31337 * y;
        n ^= 6971 * z;
        n ^= 1013 * w;
        return (float) (n * n * n * 60493) / 2.1474836E9F;
    }

    private static float GradCoord2D(int seed, int x, int y, float xd, float yd) {
        int hash = seed ^ 1619 * x;
        hash ^= 31337 * y;
        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;
        FastNoise.Float2 g = GRAD_2D[hash & 7];
        return xd * g.x + yd * g.y;
    }

    private static float GradCoord3D(int seed, int x, int y, int z, float xd, float yd, float zd) {
        int hash = seed ^ 1619 * x;
        hash ^= 31337 * y;
        hash ^= 6971 * z;
        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;
        FastNoise.Float3 g = GRAD_3D[hash & 15];
        return xd * g.x + yd * g.y + zd * g.z;
    }

    private static float GradCoord4D(int seed, int x, int y, int z, int w, float xd, float yd, float zd, float wd) {
        int hash = seed ^ 1619 * x;
        hash ^= 31337 * y;
        hash ^= 6971 * z;
        hash ^= 1013 * w;
        hash = hash * hash * hash * 60493;
        hash = hash >> 13 ^ hash;
        hash &= 31;
        float a = yd;
        float b = zd;
        float c = wd;
        switch(hash >> 3) {
            case 1:
                a = wd;
                b = xd;
                c = yd;
                break;
            case 2:
                a = zd;
                b = wd;
                c = xd;
                break;
            case 3:
                a = yd;
                b = zd;
                c = wd;
        }
        return ((hash & 4) == 0 ? -a : a) + ((hash & 2) == 0 ? -b : b) + ((hash & 1) == 0 ? -c : c);
    }

    @Override
    public float GetNoise(float x, float y, float z) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        z *= this.m_frequency;
        switch(this.m_noiseType) {
            case Value:
                return this.SingleValue(this.m_seed, x, y, z);
            case ValueFractal:
                switch(this.m_fractalType) {
                    case FBM:
                        return this.SingleValueFractalFBM(x, y, z);
                    case Billow:
                        return this.SingleValueFractalBillow(x, y, z);
                    case RigidMulti:
                        return this.SingleValueFractalRigidMulti(x, y, z);
                    default:
                        return 0.0F;
                }
            case Perlin:
                return this.SinglePerlin(this.m_seed, x, y, z);
            case PerlinFractal:
                switch(this.m_fractalType) {
                    case FBM:
                        return this.SinglePerlinFractalFBM(x, y, z);
                    case Billow:
                        return this.SinglePerlinFractalBillow(x, y, z);
                    case RigidMulti:
                        return this.SinglePerlinFractalRigidMulti(x, y, z);
                    default:
                        return 0.0F;
                }
            case Simplex:
                return this.SingleSimplex(this.m_seed, x, y, z);
            case SimplexFractal:
                switch(this.m_fractalType) {
                    case FBM:
                        return this.SingleSimplexFractalFBM(x, y, z);
                    case Billow:
                        return this.SingleSimplexFractalBillow(x, y, z);
                    case RigidMulti:
                        return this.SingleSimplexFractalRigidMulti(x, y, z);
                    default:
                        return 0.0F;
                }
            case Cellular:
                switch(this.m_cellularReturnType) {
                    case CellValue:
                    case NoiseLookup:
                    case Distance:
                        return this.SingleCellular(x, y, z);
                    default:
                        return this.SingleCellular2Edge(x, y, z);
                }
            case WhiteNoise:
                return this.GetWhiteNoise(x, y, z);
            case Cubic:
                return this.SingleCubic(this.m_seed, x, y, z);
            case CubicFractal:
                switch(this.m_fractalType) {
                    case FBM:
                        return this.SingleCubicFractalFBM(x, y, z);
                    case Billow:
                        return this.SingleCubicFractalBillow(x, y, z);
                    case RigidMulti:
                        return this.SingleCubicFractalRigidMulti(x, y, z);
                    default:
                        return 0.0F;
                }
            default:
                return 0.0F;
        }
    }

    public float GetNoise(float x, float y) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        switch(this.m_noiseType) {
            case Value:
                return this.SingleValue(this.m_seed, x, y);
            case ValueFractal:
                switch(this.m_fractalType) {
                    case FBM:
                        return this.SingleValueFractalFBM(x, y);
                    case Billow:
                        return this.SingleValueFractalBillow(x, y);
                    case RigidMulti:
                        return this.SingleValueFractalRigidMulti(x, y);
                    default:
                        return 0.0F;
                }
            case Perlin:
                return this.SinglePerlin(this.m_seed, x, y);
            case PerlinFractal:
                switch(this.m_fractalType) {
                    case FBM:
                        return this.SinglePerlinFractalFBM(x, y);
                    case Billow:
                        return this.SinglePerlinFractalBillow(x, y);
                    case RigidMulti:
                        return this.SinglePerlinFractalRigidMulti(x, y);
                    default:
                        return 0.0F;
                }
            case Simplex:
                return this.SingleSimplex(this.m_seed, x, y);
            case SimplexFractal:
                switch(this.m_fractalType) {
                    case FBM:
                        return this.SingleSimplexFractalFBM(x, y);
                    case Billow:
                        return this.SingleSimplexFractalBillow(x, y);
                    case RigidMulti:
                        return this.SingleSimplexFractalRigidMulti(x, y);
                    default:
                        return 0.0F;
                }
            case Cellular:
                switch(this.m_cellularReturnType) {
                    case CellValue:
                    case NoiseLookup:
                    case Distance:
                        return this.SingleCellular(x, y);
                    default:
                        return this.SingleCellular2Edge(x, y);
                }
            case WhiteNoise:
                return this.GetWhiteNoise(x, y);
            case Cubic:
                return this.SingleCubic(this.m_seed, x, y);
            case CubicFractal:
                switch(this.m_fractalType) {
                    case FBM:
                        return this.SingleCubicFractalFBM(x, y);
                    case Billow:
                        return this.SingleCubicFractalBillow(x, y);
                    case RigidMulti:
                        return this.SingleCubicFractalRigidMulti(x, y);
                    default:
                        return 0.0F;
                }
            default:
                return 0.0F;
        }
    }

    private int FloatCast2Int(float f) {
        int i = Float.floatToRawIntBits(f);
        return i ^ i >> 16;
    }

    public float GetWhiteNoise(float x, float y, float z, float w) {
        int xi = this.FloatCast2Int(x);
        int yi = this.FloatCast2Int(y);
        int zi = this.FloatCast2Int(z);
        int wi = this.FloatCast2Int(w);
        return ValCoord4D(this.m_seed, xi, yi, zi, wi);
    }

    public float GetWhiteNoise(float x, float y, float z) {
        int xi = this.FloatCast2Int(x);
        int yi = this.FloatCast2Int(y);
        int zi = this.FloatCast2Int(z);
        return ValCoord3D(this.m_seed, xi, yi, zi);
    }

    public float GetWhiteNoise(float x, float y) {
        int xi = this.FloatCast2Int(x);
        int yi = this.FloatCast2Int(y);
        return ValCoord2D(this.m_seed, xi, yi);
    }

    public float GetWhiteNoiseInt(int x, int y, int z, int w) {
        return ValCoord4D(this.m_seed, x, y, z, w);
    }

    public float GetWhiteNoiseInt(int x, int y, int z) {
        return ValCoord3D(this.m_seed, x, y, z);
    }

    public float GetWhiteNoiseInt(int x, int y) {
        return ValCoord2D(this.m_seed, x, y);
    }

    public float GetValueFractal(float x, float y, float z) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        z *= this.m_frequency;
        switch(this.m_fractalType) {
            case FBM:
                return this.SingleValueFractalFBM(x, y, z);
            case Billow:
                return this.SingleValueFractalBillow(x, y, z);
            case RigidMulti:
                return this.SingleValueFractalRigidMulti(x, y, z);
            default:
                return 0.0F;
        }
    }

    private float SingleValueFractalFBM(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = this.SingleValue(seed, x, y, z);
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            sum += this.SingleValue(++seed, x, y, z) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleValueFractalBillow(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = Math.abs(this.SingleValue(seed, x, y, z)) * 2.0F - 1.0F;
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum += (Math.abs(this.SingleValue(seed, x, y, z)) * 2.0F - 1.0F) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleValueFractalRigidMulti(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = 1.0F - Math.abs(this.SingleValue(seed, x, y, z));
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum -= (1.0F - Math.abs(this.SingleValue(seed, x, y, z))) * amp;
        }
        return sum;
    }

    public float GetValue(float x, float y, float z) {
        return this.SingleValue(this.m_seed, x * this.m_frequency, y * this.m_frequency, z * this.m_frequency);
    }

    private float SingleValue(int seed, float x, float y, float z) {
        int x0 = FastFloor(x);
        int y0 = FastFloor(y);
        int z0 = FastFloor(z);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        float xs;
        float ys;
        float zs;
        switch(this.m_interp) {
            case Linear:
            default:
                xs = x - (float) x0;
                ys = y - (float) y0;
                zs = z - (float) z0;
                break;
            case Hermite:
                xs = InterpHermiteFunc(x - (float) x0);
                ys = InterpHermiteFunc(y - (float) y0);
                zs = InterpHermiteFunc(z - (float) z0);
                break;
            case Quintic:
                xs = InterpQuinticFunc(x - (float) x0);
                ys = InterpQuinticFunc(y - (float) y0);
                zs = InterpQuinticFunc(z - (float) z0);
        }
        float xf00 = Lerp(ValCoord3D(seed, x0, y0, z0), ValCoord3D(seed, x1, y0, z0), xs);
        float xf10 = Lerp(ValCoord3D(seed, x0, y1, z0), ValCoord3D(seed, x1, y1, z0), xs);
        float xf01 = Lerp(ValCoord3D(seed, x0, y0, z1), ValCoord3D(seed, x1, y0, z1), xs);
        float xf11 = Lerp(ValCoord3D(seed, x0, y1, z1), ValCoord3D(seed, x1, y1, z1), xs);
        float yf0 = Lerp(xf00, xf10, ys);
        float yf1 = Lerp(xf01, xf11, ys);
        return Lerp(yf0, yf1, zs);
    }

    public float GetValueFractal(float x, float y) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        switch(this.m_fractalType) {
            case FBM:
                return this.SingleValueFractalFBM(x, y);
            case Billow:
                return this.SingleValueFractalBillow(x, y);
            case RigidMulti:
                return this.SingleValueFractalRigidMulti(x, y);
            default:
                return 0.0F;
        }
    }

    private float SingleValueFractalFBM(float x, float y) {
        int seed = this.m_seed;
        float sum = this.SingleValue(seed, x, y);
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            sum += this.SingleValue(++seed, x, y) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleValueFractalBillow(float x, float y) {
        int seed = this.m_seed;
        float sum = Math.abs(this.SingleValue(seed, x, y)) * 2.0F - 1.0F;
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum += (Math.abs(this.SingleValue(seed, x, y)) * 2.0F - 1.0F) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleValueFractalRigidMulti(float x, float y) {
        int seed = this.m_seed;
        float sum = 1.0F - Math.abs(this.SingleValue(seed, x, y));
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum -= (1.0F - Math.abs(this.SingleValue(seed, x, y))) * amp;
        }
        return sum;
    }

    public float GetValue(float x, float y) {
        return this.SingleValue(this.m_seed, x * this.m_frequency, y * this.m_frequency);
    }

    private float SingleValue(int seed, float x, float y) {
        int x0 = FastFloor(x);
        int y0 = FastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        float xs;
        float ys;
        switch(this.m_interp) {
            case Linear:
            default:
                xs = x - (float) x0;
                ys = y - (float) y0;
                break;
            case Hermite:
                xs = InterpHermiteFunc(x - (float) x0);
                ys = InterpHermiteFunc(y - (float) y0);
                break;
            case Quintic:
                xs = InterpQuinticFunc(x - (float) x0);
                ys = InterpQuinticFunc(y - (float) y0);
        }
        float xf0 = Lerp(ValCoord2D(seed, x0, y0), ValCoord2D(seed, x1, y0), xs);
        float xf1 = Lerp(ValCoord2D(seed, x0, y1), ValCoord2D(seed, x1, y1), xs);
        return Lerp(xf0, xf1, ys);
    }

    public float GetPerlinFractal(float x, float y, float z) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        z *= this.m_frequency;
        switch(this.m_fractalType) {
            case FBM:
                return this.SinglePerlinFractalFBM(x, y, z);
            case Billow:
                return this.SinglePerlinFractalBillow(x, y, z);
            case RigidMulti:
                return this.SinglePerlinFractalRigidMulti(x, y, z);
            default:
                return 0.0F;
        }
    }

    private float SinglePerlinFractalFBM(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = this.SinglePerlin(seed, x, y, z);
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            sum += this.SinglePerlin(++seed, x, y, z) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SinglePerlinFractalBillow(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = Math.abs(this.SinglePerlin(seed, x, y, z)) * 2.0F - 1.0F;
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum += (Math.abs(this.SinglePerlin(seed, x, y, z)) * 2.0F - 1.0F) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SinglePerlinFractalRigidMulti(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = 1.0F - Math.abs(this.SinglePerlin(seed, x, y, z));
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum -= (1.0F - Math.abs(this.SinglePerlin(seed, x, y, z))) * amp;
        }
        return sum;
    }

    public float GetPerlin(float x, float y, float z) {
        return this.SinglePerlin(this.m_seed, x * this.m_frequency, y * this.m_frequency, z * this.m_frequency);
    }

    private float SinglePerlin(int seed, float x, float y, float z) {
        int x0 = FastFloor(x);
        int y0 = FastFloor(y);
        int z0 = FastFloor(z);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        float xs;
        float ys;
        float zs;
        switch(this.m_interp) {
            case Linear:
            default:
                xs = x - (float) x0;
                ys = y - (float) y0;
                zs = z - (float) z0;
                break;
            case Hermite:
                xs = InterpHermiteFunc(x - (float) x0);
                ys = InterpHermiteFunc(y - (float) y0);
                zs = InterpHermiteFunc(z - (float) z0);
                break;
            case Quintic:
                xs = InterpQuinticFunc(x - (float) x0);
                ys = InterpQuinticFunc(y - (float) y0);
                zs = InterpQuinticFunc(z - (float) z0);
        }
        float xd0 = x - (float) x0;
        float yd0 = y - (float) y0;
        float zd0 = z - (float) z0;
        float xd1 = xd0 - 1.0F;
        float yd1 = yd0 - 1.0F;
        float zd1 = zd0 - 1.0F;
        float xf00 = Lerp(GradCoord3D(seed, x0, y0, z0, xd0, yd0, zd0), GradCoord3D(seed, x1, y0, z0, xd1, yd0, zd0), xs);
        float xf10 = Lerp(GradCoord3D(seed, x0, y1, z0, xd0, yd1, zd0), GradCoord3D(seed, x1, y1, z0, xd1, yd1, zd0), xs);
        float xf01 = Lerp(GradCoord3D(seed, x0, y0, z1, xd0, yd0, zd1), GradCoord3D(seed, x1, y0, z1, xd1, yd0, zd1), xs);
        float xf11 = Lerp(GradCoord3D(seed, x0, y1, z1, xd0, yd1, zd1), GradCoord3D(seed, x1, y1, z1, xd1, yd1, zd1), xs);
        float yf0 = Lerp(xf00, xf10, ys);
        float yf1 = Lerp(xf01, xf11, ys);
        return Lerp(yf0, yf1, zs);
    }

    public float GetPerlinFractal(float x, float y) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        switch(this.m_fractalType) {
            case FBM:
                return this.SinglePerlinFractalFBM(x, y);
            case Billow:
                return this.SinglePerlinFractalBillow(x, y);
            case RigidMulti:
                return this.SinglePerlinFractalRigidMulti(x, y);
            default:
                return 0.0F;
        }
    }

    private float SinglePerlinFractalFBM(float x, float y) {
        int seed = this.m_seed;
        float sum = this.SinglePerlin(seed, x, y);
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            sum += this.SinglePerlin(++seed, x, y) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SinglePerlinFractalBillow(float x, float y) {
        int seed = this.m_seed;
        float sum = Math.abs(this.SinglePerlin(seed, x, y)) * 2.0F - 1.0F;
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum += (Math.abs(this.SinglePerlin(seed, x, y)) * 2.0F - 1.0F) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SinglePerlinFractalRigidMulti(float x, float y) {
        int seed = this.m_seed;
        float sum = 1.0F - Math.abs(this.SinglePerlin(seed, x, y));
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum -= (1.0F - Math.abs(this.SinglePerlin(seed, x, y))) * amp;
        }
        return sum;
    }

    public float GetPerlin(float x, float y) {
        return this.SinglePerlin(this.m_seed, x * this.m_frequency, y * this.m_frequency);
    }

    private float SinglePerlin(int seed, float x, float y) {
        int x0 = FastFloor(x);
        int y0 = FastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        float xs;
        float ys;
        switch(this.m_interp) {
            case Linear:
            default:
                xs = x - (float) x0;
                ys = y - (float) y0;
                break;
            case Hermite:
                xs = InterpHermiteFunc(x - (float) x0);
                ys = InterpHermiteFunc(y - (float) y0);
                break;
            case Quintic:
                xs = InterpQuinticFunc(x - (float) x0);
                ys = InterpQuinticFunc(y - (float) y0);
        }
        float xd0 = x - (float) x0;
        float yd0 = y - (float) y0;
        float xd1 = xd0 - 1.0F;
        float yd1 = yd0 - 1.0F;
        float xf0 = Lerp(GradCoord2D(seed, x0, y0, xd0, yd0), GradCoord2D(seed, x1, y0, xd1, yd0), xs);
        float xf1 = Lerp(GradCoord2D(seed, x0, y1, xd0, yd1), GradCoord2D(seed, x1, y1, xd1, yd1), xs);
        return Lerp(xf0, xf1, ys);
    }

    public float GetSimplexFractal(float x, float y, float z) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        z *= this.m_frequency;
        switch(this.m_fractalType) {
            case FBM:
                return this.SingleSimplexFractalFBM(x, y, z);
            case Billow:
                return this.SingleSimplexFractalBillow(x, y, z);
            case RigidMulti:
                return this.SingleSimplexFractalRigidMulti(x, y, z);
            default:
                return 0.0F;
        }
    }

    private float SingleSimplexFractalFBM(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = this.SingleSimplex(seed, x, y, z);
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            sum += this.SingleSimplex(++seed, x, y, z) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleSimplexFractalBillow(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = Math.abs(this.SingleSimplex(seed, x, y, z)) * 2.0F - 1.0F;
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum += (Math.abs(this.SingleSimplex(seed, x, y, z)) * 2.0F - 1.0F) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleSimplexFractalRigidMulti(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = 1.0F - Math.abs(this.SingleSimplex(seed, x, y, z));
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum -= (1.0F - Math.abs(this.SingleSimplex(seed, x, y, z))) * amp;
        }
        return sum;
    }

    public float GetSimplex(float x, float y, float z) {
        return this.SingleSimplex(this.m_seed, x * this.m_frequency, y * this.m_frequency, z * this.m_frequency);
    }

    private float SingleSimplex(int seed, float x, float y, float z) {
        float t = (x + y + z) * 0.33333334F;
        int i = FastFloor(x + t);
        int j = FastFloor(y + t);
        int k = FastFloor(z + t);
        t = (float) (i + j + k) * 0.16666667F;
        float x0 = x - ((float) i - t);
        float y0 = y - ((float) j - t);
        float z0 = z - ((float) k - t);
        int i1;
        int j1;
        int k1;
        int i2;
        int j2;
        int k2;
        if (x0 >= y0) {
            if (y0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            } else if (x0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            } else {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            }
        } else if (y0 < z0) {
            i1 = 0;
            j1 = 0;
            k1 = 1;
            i2 = 0;
            j2 = 1;
            k2 = 1;
        } else if (x0 < z0) {
            i1 = 0;
            j1 = 1;
            k1 = 0;
            i2 = 0;
            j2 = 1;
            k2 = 1;
        } else {
            i1 = 0;
            j1 = 1;
            k1 = 0;
            i2 = 1;
            j2 = 1;
            k2 = 0;
        }
        float x1 = x0 - (float) i1 + 0.16666667F;
        float y1 = y0 - (float) j1 + 0.16666667F;
        float z1 = z0 - (float) k1 + 0.16666667F;
        float x2 = x0 - (float) i2 + 0.33333334F;
        float y2 = y0 - (float) j2 + 0.33333334F;
        float z2 = z0 - (float) k2 + 0.33333334F;
        float x3 = x0 + -0.5F;
        float y3 = y0 + -0.5F;
        float z3 = z0 + -0.5F;
        t = 0.6F - x0 * x0 - y0 * y0 - z0 * z0;
        float n0;
        if (t < 0.0F) {
            n0 = 0.0F;
        } else {
            t *= t;
            n0 = t * t * GradCoord3D(seed, i, j, k, x0, y0, z0);
        }
        t = 0.6F - x1 * x1 - y1 * y1 - z1 * z1;
        float n1;
        if (t < 0.0F) {
            n1 = 0.0F;
        } else {
            t *= t;
            n1 = t * t * GradCoord3D(seed, i + i1, j + j1, k + k1, x1, y1, z1);
        }
        t = 0.6F - x2 * x2 - y2 * y2 - z2 * z2;
        float n2;
        if (t < 0.0F) {
            n2 = 0.0F;
        } else {
            t *= t;
            n2 = t * t * GradCoord3D(seed, i + i2, j + j2, k + k2, x2, y2, z2);
        }
        t = 0.6F - x3 * x3 - y3 * y3 - z3 * z3;
        float n3;
        if (t < 0.0F) {
            n3 = 0.0F;
        } else {
            t *= t;
            n3 = t * t * GradCoord3D(seed, i + 1, j + 1, k + 1, x3, y3, z3);
        }
        return 32.0F * (n0 + n1 + n2 + n3);
    }

    public float GetSimplexFractal(float x, float y) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        switch(this.m_fractalType) {
            case FBM:
                return this.SingleSimplexFractalFBM(x, y);
            case Billow:
                return this.SingleSimplexFractalBillow(x, y);
            case RigidMulti:
                return this.SingleSimplexFractalRigidMulti(x, y);
            default:
                return 0.0F;
        }
    }

    private float SingleSimplexFractalFBM(float x, float y) {
        int seed = this.m_seed;
        float sum = this.SingleSimplex(seed, x, y);
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            sum += this.SingleSimplex(++seed, x, y) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleSimplexFractalBillow(float x, float y) {
        int seed = this.m_seed;
        float sum = Math.abs(this.SingleSimplex(seed, x, y)) * 2.0F - 1.0F;
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum += (Math.abs(this.SingleSimplex(seed, x, y)) * 2.0F - 1.0F) * amp;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleSimplexFractalRigidMulti(float x, float y) {
        int seed = this.m_seed;
        float sum = 1.0F - Math.abs(this.SingleSimplex(seed, x, y));
        float amp = 1.0F;
        for (int i = 1; i < this.m_octaves; i++) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
            sum -= (1.0F - Math.abs(this.SingleSimplex(seed, x, y))) * amp;
        }
        return sum;
    }

    public float GetSimplex(float x, float y) {
        return this.SingleSimplex(this.m_seed, x * this.m_frequency, y * this.m_frequency);
    }

    private float SingleSimplex(int seed, float x, float y) {
        float t = (x + y) * 0.5F;
        int i = FastFloor(x + t);
        int j = FastFloor(y + t);
        t = (float) (i + j) * 0.25F;
        float X0 = (float) i - t;
        float Y0 = (float) j - t;
        float x0 = x - X0;
        float y0 = y - Y0;
        int i1;
        int j1;
        if (x0 > y0) {
            i1 = 1;
            j1 = 0;
        } else {
            i1 = 0;
            j1 = 1;
        }
        float x1 = x0 - (float) i1 + 0.25F;
        float y1 = y0 - (float) j1 + 0.25F;
        float x2 = x0 - 1.0F + 0.5F;
        float y2 = y0 - 1.0F + 0.5F;
        t = 0.5F - x0 * x0 - y0 * y0;
        float n0;
        if (t < 0.0F) {
            n0 = 0.0F;
        } else {
            t *= t;
            n0 = t * t * GradCoord2D(seed, i, j, x0, y0);
        }
        t = 0.5F - x1 * x1 - y1 * y1;
        float n1;
        if (t < 0.0F) {
            n1 = 0.0F;
        } else {
            t *= t;
            n1 = t * t * GradCoord2D(seed, i + i1, j + j1, x1, y1);
        }
        t = 0.5F - x2 * x2 - y2 * y2;
        float n2;
        if (t < 0.0F) {
            n2 = 0.0F;
        } else {
            t *= t;
            n2 = t * t * GradCoord2D(seed, i + 1, j + 1, x2, y2);
        }
        return 50.0F * (n0 + n1 + n2);
    }

    public float GetSimplex(float x, float y, float z, float w) {
        return this.SingleSimplex(this.m_seed, x * this.m_frequency, y * this.m_frequency, z * this.m_frequency, w * this.m_frequency);
    }

    private float SingleSimplex(int seed, float x, float y, float z, float w) {
        float t = (x + y + z + w) * 0.309017F;
        int i = FastFloor(x + t);
        int j = FastFloor(y + t);
        int k = FastFloor(z + t);
        int l = FastFloor(w + t);
        t = (float) (i + j + k + l) * 0.1381966F;
        float X0 = (float) i - t;
        float Y0 = (float) j - t;
        float Z0 = (float) k - t;
        float W0 = (float) l - t;
        float x0 = x - X0;
        float y0 = y - Y0;
        float z0 = z - Z0;
        float w0 = w - W0;
        int c = x0 > y0 ? 32 : 0;
        c += x0 > z0 ? 16 : 0;
        c += y0 > z0 ? 8 : 0;
        c += x0 > w0 ? 4 : 0;
        c += y0 > w0 ? 2 : 0;
        c += z0 > w0 ? 1 : 0;
        c <<= 2;
        int i1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
        int i2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
        int i3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
        int j1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
        int j2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
        int j3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
        int k1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
        int k2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
        int k3 = SIMPLEX_4D[c++] >= 1 ? 1 : 0;
        int l1 = SIMPLEX_4D[c] >= 3 ? 1 : 0;
        int l2 = SIMPLEX_4D[c] >= 2 ? 1 : 0;
        int l3 = SIMPLEX_4D[c] >= 1 ? 1 : 0;
        float x1 = x0 - (float) i1 + 0.1381966F;
        float y1 = y0 - (float) j1 + 0.1381966F;
        float z1 = z0 - (float) k1 + 0.1381966F;
        float w1 = w0 - (float) l1 + 0.1381966F;
        float x2 = x0 - (float) i2 + 0.2763932F;
        float y2 = y0 - (float) j2 + 0.2763932F;
        float z2 = z0 - (float) k2 + 0.2763932F;
        float w2 = w0 - (float) l2 + 0.2763932F;
        float x3 = x0 - (float) i3 + 0.41458982F;
        float y3 = y0 - (float) j3 + 0.41458982F;
        float z3 = z0 - (float) k3 + 0.41458982F;
        float w3 = w0 - (float) l3 + 0.41458982F;
        float x4 = x0 - 1.0F + 0.5527864F;
        float y4 = y0 - 1.0F + 0.5527864F;
        float z4 = z0 - 1.0F + 0.5527864F;
        float w4 = w0 - 1.0F + 0.5527864F;
        t = 0.6F - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
        float n0;
        if (t < 0.0F) {
            n0 = 0.0F;
        } else {
            t *= t;
            n0 = t * t * GradCoord4D(seed, i, j, k, l, x0, y0, z0, w0);
        }
        t = 0.6F - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
        float n1;
        if (t < 0.0F) {
            n1 = 0.0F;
        } else {
            t *= t;
            n1 = t * t * GradCoord4D(seed, i + i1, j + j1, k + k1, l + l1, x1, y1, z1, w1);
        }
        t = 0.6F - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
        float n2;
        if (t < 0.0F) {
            n2 = 0.0F;
        } else {
            t *= t;
            n2 = t * t * GradCoord4D(seed, i + i2, j + j2, k + k2, l + l2, x2, y2, z2, w2);
        }
        t = 0.6F - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
        float n3;
        if (t < 0.0F) {
            n3 = 0.0F;
        } else {
            t *= t;
            n3 = t * t * GradCoord4D(seed, i + i3, j + j3, k + k3, l + l3, x3, y3, z3, w3);
        }
        t = 0.6F - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
        float n4;
        if (t < 0.0F) {
            n4 = 0.0F;
        } else {
            t *= t;
            n4 = t * t * GradCoord4D(seed, i + 1, j + 1, k + 1, l + 1, x4, y4, z4, w4);
        }
        return 27.0F * (n0 + n1 + n2 + n3 + n4);
    }

    public float GetCubicFractal(float x, float y, float z) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        z *= this.m_frequency;
        switch(this.m_fractalType) {
            case FBM:
                return this.SingleCubicFractalFBM(x, y, z);
            case Billow:
                return this.SingleCubicFractalBillow(x, y, z);
            case RigidMulti:
                return this.SingleCubicFractalRigidMulti(x, y, z);
            default:
                return 0.0F;
        }
    }

    private float SingleCubicFractalFBM(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = this.SingleCubic(seed, x, y, z);
        float amp = 1.0F;
        for (int i = 0; ++i < this.m_octaves; sum += this.SingleCubic(++seed, x, y, z) * amp) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleCubicFractalBillow(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = Math.abs(this.SingleCubic(seed, x, y, z)) * 2.0F - 1.0F;
        float amp = 1.0F;
        for (int i = 0; ++i < this.m_octaves; sum += (Math.abs(this.SingleCubic(seed, x, y, z)) * 2.0F - 1.0F) * amp) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleCubicFractalRigidMulti(float x, float y, float z) {
        int seed = this.m_seed;
        float sum = 1.0F - Math.abs(this.SingleCubic(seed, x, y, z));
        float amp = 1.0F;
        for (int i = 0; ++i < this.m_octaves; sum -= (1.0F - Math.abs(this.SingleCubic(seed, x, y, z))) * amp) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            z *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
        }
        return sum;
    }

    public float GetCubic(float x, float y, float z) {
        return this.SingleCubic(this.m_seed, x * this.m_frequency, y * this.m_frequency, z * this.m_frequency);
    }

    private float SingleCubic(int seed, float x, float y, float z) {
        int x1 = FastFloor(x);
        int y1 = FastFloor(y);
        int z1 = FastFloor(z);
        int x0 = x1 - 1;
        int y0 = y1 - 1;
        int z0 = z1 - 1;
        int x2 = x1 + 1;
        int y2 = y1 + 1;
        int z2 = z1 + 1;
        int x3 = x1 + 2;
        int y3 = y1 + 2;
        int z3 = z1 + 2;
        float xs = x - (float) x1;
        float ys = y - (float) y1;
        float zs = z - (float) z1;
        return CubicLerp(CubicLerp(CubicLerp(ValCoord3D(seed, x0, y0, z0), ValCoord3D(seed, x1, y0, z0), ValCoord3D(seed, x2, y0, z0), ValCoord3D(seed, x3, y0, z0), xs), CubicLerp(ValCoord3D(seed, x0, y1, z0), ValCoord3D(seed, x1, y1, z0), ValCoord3D(seed, x2, y1, z0), ValCoord3D(seed, x3, y1, z0), xs), CubicLerp(ValCoord3D(seed, x0, y2, z0), ValCoord3D(seed, x1, y2, z0), ValCoord3D(seed, x2, y2, z0), ValCoord3D(seed, x3, y2, z0), xs), CubicLerp(ValCoord3D(seed, x0, y3, z0), ValCoord3D(seed, x1, y3, z0), ValCoord3D(seed, x2, y3, z0), ValCoord3D(seed, x3, y3, z0), xs), ys), CubicLerp(CubicLerp(ValCoord3D(seed, x0, y0, z1), ValCoord3D(seed, x1, y0, z1), ValCoord3D(seed, x2, y0, z1), ValCoord3D(seed, x3, y0, z1), xs), CubicLerp(ValCoord3D(seed, x0, y1, z1), ValCoord3D(seed, x1, y1, z1), ValCoord3D(seed, x2, y1, z1), ValCoord3D(seed, x3, y1, z1), xs), CubicLerp(ValCoord3D(seed, x0, y2, z1), ValCoord3D(seed, x1, y2, z1), ValCoord3D(seed, x2, y2, z1), ValCoord3D(seed, x3, y2, z1), xs), CubicLerp(ValCoord3D(seed, x0, y3, z1), ValCoord3D(seed, x1, y3, z1), ValCoord3D(seed, x2, y3, z1), ValCoord3D(seed, x3, y3, z1), xs), ys), CubicLerp(CubicLerp(ValCoord3D(seed, x0, y0, z2), ValCoord3D(seed, x1, y0, z2), ValCoord3D(seed, x2, y0, z2), ValCoord3D(seed, x3, y0, z2), xs), CubicLerp(ValCoord3D(seed, x0, y1, z2), ValCoord3D(seed, x1, y1, z2), ValCoord3D(seed, x2, y1, z2), ValCoord3D(seed, x3, y1, z2), xs), CubicLerp(ValCoord3D(seed, x0, y2, z2), ValCoord3D(seed, x1, y2, z2), ValCoord3D(seed, x2, y2, z2), ValCoord3D(seed, x3, y2, z2), xs), CubicLerp(ValCoord3D(seed, x0, y3, z2), ValCoord3D(seed, x1, y3, z2), ValCoord3D(seed, x2, y3, z2), ValCoord3D(seed, x3, y3, z2), xs), ys), CubicLerp(CubicLerp(ValCoord3D(seed, x0, y0, z3), ValCoord3D(seed, x1, y0, z3), ValCoord3D(seed, x2, y0, z3), ValCoord3D(seed, x3, y0, z3), xs), CubicLerp(ValCoord3D(seed, x0, y1, z3), ValCoord3D(seed, x1, y1, z3), ValCoord3D(seed, x2, y1, z3), ValCoord3D(seed, x3, y1, z3), xs), CubicLerp(ValCoord3D(seed, x0, y2, z3), ValCoord3D(seed, x1, y2, z3), ValCoord3D(seed, x2, y2, z3), ValCoord3D(seed, x3, y2, z3), xs), CubicLerp(ValCoord3D(seed, x0, y3, z3), ValCoord3D(seed, x1, y3, z3), ValCoord3D(seed, x2, y3, z3), ValCoord3D(seed, x3, y3, z3), xs), ys), zs) * 0.2962963F;
    }

    public float GetCubicFractal(float x, float y) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        switch(this.m_fractalType) {
            case FBM:
                return this.SingleCubicFractalFBM(x, y);
            case Billow:
                return this.SingleCubicFractalBillow(x, y);
            case RigidMulti:
                return this.SingleCubicFractalRigidMulti(x, y);
            default:
                return 0.0F;
        }
    }

    private float SingleCubicFractalFBM(float x, float y) {
        int seed = this.m_seed;
        float sum = this.SingleCubic(seed, x, y);
        float amp = 1.0F;
        for (int i = 0; ++i < this.m_octaves; sum += this.SingleCubic(++seed, x, y) * amp) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleCubicFractalBillow(float x, float y) {
        int seed = this.m_seed;
        float sum = Math.abs(this.SingleCubic(seed, x, y)) * 2.0F - 1.0F;
        float amp = 1.0F;
        for (int i = 0; ++i < this.m_octaves; sum += (Math.abs(this.SingleCubic(seed, x, y)) * 2.0F - 1.0F) * amp) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
        }
        return sum * this.m_fractalBounding;
    }

    private float SingleCubicFractalRigidMulti(float x, float y) {
        int seed = this.m_seed;
        float sum = 1.0F - Math.abs(this.SingleCubic(seed, x, y));
        float amp = 1.0F;
        for (int i = 0; ++i < this.m_octaves; sum -= (1.0F - Math.abs(this.SingleCubic(seed, x, y))) * amp) {
            x *= this.m_lacunarity;
            y *= this.m_lacunarity;
            amp *= this.m_gain;
            seed++;
        }
        return sum;
    }

    public float GetCubic(float x, float y) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        return this.SingleCubic(0, x, y);
    }

    private float SingleCubic(int seed, float x, float y) {
        int x1 = FastFloor(x);
        int y1 = FastFloor(y);
        int x0 = x1 - 1;
        int y0 = y1 - 1;
        int x2 = x1 + 1;
        int y2 = y1 + 1;
        int x3 = x1 + 2;
        int y3 = y1 + 2;
        float xs = x - (float) x1;
        float ys = y - (float) y1;
        return CubicLerp(CubicLerp(ValCoord2D(seed, x0, y0), ValCoord2D(seed, x1, y0), ValCoord2D(seed, x2, y0), ValCoord2D(seed, x3, y0), xs), CubicLerp(ValCoord2D(seed, x0, y1), ValCoord2D(seed, x1, y1), ValCoord2D(seed, x2, y1), ValCoord2D(seed, x3, y1), xs), CubicLerp(ValCoord2D(seed, x0, y2), ValCoord2D(seed, x1, y2), ValCoord2D(seed, x2, y2), ValCoord2D(seed, x3, y2), xs), CubicLerp(ValCoord2D(seed, x0, y3), ValCoord2D(seed, x1, y3), ValCoord2D(seed, x2, y3), ValCoord2D(seed, x3, y3), xs), ys) * 0.44444445F;
    }

    public float GetCellular(float x, float y, float z) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        z *= this.m_frequency;
        switch(this.m_cellularReturnType) {
            case CellValue:
            case NoiseLookup:
            case Distance:
                return this.SingleCellular(x, y, z);
            default:
                return this.SingleCellular2Edge(x, y, z);
        }
    }

    private float SingleCellular(float x, float y, float z) {
        int xr = FastRound(x);
        int yr = FastRound(y);
        int zr = FastRound(z);
        float distance = 999999.0F;
        int xc = 0;
        int yc = 0;
        int zc = 0;
        switch(this.m_cellularDistanceFunction) {
            case Euclidean:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zixx = zr - 1; zixx <= zr + 1; zixx++) {
                            FastNoise.Float3 vec = CELL_3D[Hash3D(this.m_seed, xi, yi, zixx) & 0xFF];
                            float vecX = (float) xi - x + vec.x;
                            float vecY = (float) yi - y + vec.y;
                            float vecZ = (float) zixx - z + vec.z;
                            float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;
                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                                zc = zixx;
                            }
                        }
                    }
                }
                break;
            case Manhattan:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zix = zr - 1; zix <= zr + 1; zix++) {
                            FastNoise.Float3 vec = CELL_3D[Hash3D(this.m_seed, xi, yi, zix) & 0xFF];
                            float vecX = (float) xi - x + vec.x;
                            float vecY = (float) yi - y + vec.y;
                            float vecZ = (float) zix - z + vec.z;
                            float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);
                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                                zc = zix;
                            }
                        }
                    }
                }
                break;
            case Natural:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            FastNoise.Float3 vec = CELL_3D[Hash3D(this.m_seed, xi, yi, zi) & 0xFF];
                            float vecX = (float) xi - x + vec.x;
                            float vecY = (float) yi - y + vec.y;
                            float vecZ = (float) zi - z + vec.z;
                            float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ) + vecX * vecX + vecY * vecY + vecZ * vecZ;
                            if (newDistance < distance) {
                                distance = newDistance;
                                xc = xi;
                                yc = yi;
                                zc = zi;
                            }
                        }
                    }
                }
        }
        switch(this.m_cellularReturnType) {
            case CellValue:
                return ValCoord3D(0, xc, yc, zc);
            case NoiseLookup:
                FastNoise.Float3 vec = CELL_3D[Hash3D(this.m_seed, xc, yc, zc) & 0xFF];
                return this.m_cellularNoiseLookup.GetNoise((float) xc + vec.x, (float) yc + vec.y, (float) zc + vec.z);
            case Distance:
                return distance - 1.0F;
            default:
                return 0.0F;
        }
    }

    private float SingleCellular2Edge(float x, float y, float z) {
        int xr = FastRound(x);
        int yr = FastRound(y);
        int zr = FastRound(z);
        float distance = 999999.0F;
        float distance2 = 999999.0F;
        switch(this.m_cellularDistanceFunction) {
            case Euclidean:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            FastNoise.Float3 vec = CELL_3D[Hash3D(this.m_seed, xi, yi, zi) & 0xFF];
                            float vecX = (float) xi - x + vec.x;
                            float vecY = (float) yi - y + vec.y;
                            float vecZ = (float) zi - z + vec.z;
                            float newDistance = vecX * vecX + vecY * vecY + vecZ * vecZ;
                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
                        }
                    }
                }
                break;
            case Manhattan:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            FastNoise.Float3 vec = CELL_3D[Hash3D(this.m_seed, xi, yi, zi) & 0xFF];
                            float vecX = (float) xi - x + vec.x;
                            float vecY = (float) yi - y + vec.y;
                            float vecZ = (float) zi - z + vec.z;
                            float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ);
                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
                        }
                    }
                }
                break;
            case Natural:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        for (int zi = zr - 1; zi <= zr + 1; zi++) {
                            FastNoise.Float3 vec = CELL_3D[Hash3D(this.m_seed, xi, yi, zi) & 0xFF];
                            float vecX = (float) xi - x + vec.x;
                            float vecY = (float) yi - y + vec.y;
                            float vecZ = (float) zi - z + vec.z;
                            float newDistance = Math.abs(vecX) + Math.abs(vecY) + Math.abs(vecZ) + vecX * vecX + vecY * vecY + vecZ * vecZ;
                            distance2 = Math.max(Math.min(distance2, newDistance), distance);
                            distance = Math.min(distance, newDistance);
                        }
                    }
                }
        }
        switch(this.m_cellularReturnType) {
            case Distance2:
                return distance2 - 1.0F;
            case Distance2Add:
                return distance2 + distance - 1.0F;
            case Distance2Sub:
                return distance2 - distance - 1.0F;
            case Distance2Mul:
                return distance2 * distance - 1.0F;
            case Distance2Div:
                return distance / distance2 - 1.0F;
            default:
                return 0.0F;
        }
    }

    public float GetCellular(float x, float y) {
        x *= this.m_frequency;
        y *= this.m_frequency;
        switch(this.m_cellularReturnType) {
            case CellValue:
            case NoiseLookup:
            case Distance:
                return this.SingleCellular(x, y);
            default:
                return this.SingleCellular2Edge(x, y);
        }
    }

    private float SingleCellular(float x, float y) {
        int xr = FastRound(x);
        int yr = FastRound(y);
        float distance = 999999.0F;
        int xc = 0;
        int yc = 0;
        switch(this.m_cellularDistanceFunction) {
            case Euclidean:
            default:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yixx = yr - 1; yixx <= yr + 1; yixx++) {
                        FastNoise.Float2 vec = CELL_2D[Hash2D(this.m_seed, xi, yixx) & 0xFF];
                        float vecX = (float) xi - x + vec.x;
                        float vecY = (float) yixx - y + vec.y;
                        float newDistance = vecX * vecX + vecY * vecY;
                        if (newDistance < distance) {
                            distance = newDistance;
                            xc = xi;
                            yc = yixx;
                        }
                    }
                }
                break;
            case Manhattan:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yix = yr - 1; yix <= yr + 1; yix++) {
                        FastNoise.Float2 vec = CELL_2D[Hash2D(this.m_seed, xi, yix) & 0xFF];
                        float vecX = (float) xi - x + vec.x;
                        float vecY = (float) yix - y + vec.y;
                        float newDistance = Math.abs(vecX) + Math.abs(vecY);
                        if (newDistance < distance) {
                            distance = newDistance;
                            xc = xi;
                            yc = yix;
                        }
                    }
                }
                break;
            case Natural:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        FastNoise.Float2 vec = CELL_2D[Hash2D(this.m_seed, xi, yi) & 0xFF];
                        float vecX = (float) xi - x + vec.x;
                        float vecY = (float) yi - y + vec.y;
                        float newDistance = Math.abs(vecX) + Math.abs(vecY) + vecX * vecX + vecY * vecY;
                        if (newDistance < distance) {
                            distance = newDistance;
                            xc = xi;
                            yc = yi;
                        }
                    }
                }
        }
        switch(this.m_cellularReturnType) {
            case CellValue:
                return ValCoord2D(0, xc, yc);
            case NoiseLookup:
                FastNoise.Float2 vec = CELL_2D[Hash2D(this.m_seed, xc, yc) & 0xFF];
                return this.m_cellularNoiseLookup.GetNoise((float) xc + vec.x, (float) yc + vec.y);
            case Distance:
                return distance - 1.0F;
            default:
                return 0.0F;
        }
    }

    private float SingleCellular2Edge(float x, float y) {
        int xr = FastRound(x);
        int yr = FastRound(y);
        float distance = 999999.0F;
        float distance2 = 999999.0F;
        switch(this.m_cellularDistanceFunction) {
            case Euclidean:
            default:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        FastNoise.Float2 vec = CELL_2D[Hash2D(this.m_seed, xi, yi) & 0xFF];
                        float vecX = (float) xi - x + vec.x;
                        float vecY = (float) yi - y + vec.y;
                        float newDistance = vecX * vecX + vecY * vecY;
                        distance2 = Math.max(Math.min(distance2, newDistance), distance);
                        distance = Math.min(distance, newDistance);
                    }
                }
                break;
            case Manhattan:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        FastNoise.Float2 vec = CELL_2D[Hash2D(this.m_seed, xi, yi) & 0xFF];
                        float vecX = (float) xi - x + vec.x;
                        float vecY = (float) yi - y + vec.y;
                        float newDistance = Math.abs(vecX) + Math.abs(vecY);
                        distance2 = Math.max(Math.min(distance2, newDistance), distance);
                        distance = Math.min(distance, newDistance);
                    }
                }
                break;
            case Natural:
                for (int xi = xr - 1; xi <= xr + 1; xi++) {
                    for (int yi = yr - 1; yi <= yr + 1; yi++) {
                        FastNoise.Float2 vec = CELL_2D[Hash2D(this.m_seed, xi, yi) & 0xFF];
                        float vecX = (float) xi - x + vec.x;
                        float vecY = (float) yi - y + vec.y;
                        float newDistance = Math.abs(vecX) + Math.abs(vecY) + vecX * vecX + vecY * vecY;
                        distance2 = Math.max(Math.min(distance2, newDistance), distance);
                        distance = Math.min(distance, newDistance);
                    }
                }
        }
        switch(this.m_cellularReturnType) {
            case Distance2:
                return distance2 - 1.0F;
            case Distance2Add:
                return distance2 + distance - 1.0F;
            case Distance2Sub:
                return distance2 - distance - 1.0F;
            case Distance2Mul:
                return distance2 * distance - 1.0F;
            case Distance2Div:
                return distance / distance2 - 1.0F;
            default:
                return 0.0F;
        }
    }

    public void GradientPerturb(Vector3f v3) {
        this.SingleGradientPerturb(this.m_seed, this.m_gradientPerturbAmp, this.m_frequency, v3);
    }

    public void GradientPerturbFractal(Vector3f v3) {
        int seed = this.m_seed;
        float amp = this.m_gradientPerturbAmp * this.m_fractalBounding;
        float freq = this.m_frequency;
        this.SingleGradientPerturb(seed, amp, this.m_frequency, v3);
        for (int i = 1; i < this.m_octaves; i++) {
            freq *= this.m_lacunarity;
            amp *= this.m_gain;
            this.SingleGradientPerturb(++seed, amp, freq, v3);
        }
    }

    private void SingleGradientPerturb(int seed, float perturbAmp, float frequency, Vector3f v3) {
        float xf = v3.x * frequency;
        float yf = v3.y * frequency;
        float zf = v3.z * frequency;
        int x0 = FastFloor(xf);
        int y0 = FastFloor(yf);
        int z0 = FastFloor(zf);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        float xs;
        float ys;
        float zs;
        switch(this.m_interp) {
            case Linear:
            default:
                xs = xf - (float) x0;
                ys = yf - (float) y0;
                zs = zf - (float) z0;
                break;
            case Hermite:
                xs = InterpHermiteFunc(xf - (float) x0);
                ys = InterpHermiteFunc(yf - (float) y0);
                zs = InterpHermiteFunc(zf - (float) z0);
                break;
            case Quintic:
                xs = InterpQuinticFunc(xf - (float) x0);
                ys = InterpQuinticFunc(yf - (float) y0);
                zs = InterpQuinticFunc(zf - (float) z0);
        }
        FastNoise.Float3 vec0 = CELL_3D[Hash3D(seed, x0, y0, z0) & 0xFF];
        FastNoise.Float3 vec1 = CELL_3D[Hash3D(seed, x1, y0, z0) & 0xFF];
        float lx0x = Lerp(vec0.x, vec1.x, xs);
        float ly0x = Lerp(vec0.y, vec1.y, xs);
        float lz0x = Lerp(vec0.z, vec1.z, xs);
        vec0 = CELL_3D[Hash3D(seed, x0, y1, z0) & 0xFF];
        vec1 = CELL_3D[Hash3D(seed, x1, y1, z0) & 0xFF];
        float lx1x = Lerp(vec0.x, vec1.x, xs);
        float ly1x = Lerp(vec0.y, vec1.y, xs);
        float lz1x = Lerp(vec0.z, vec1.z, xs);
        float lx0y = Lerp(lx0x, lx1x, ys);
        float ly0y = Lerp(ly0x, ly1x, ys);
        float lz0y = Lerp(lz0x, lz1x, ys);
        vec0 = CELL_3D[Hash3D(seed, x0, y0, z1) & 0xFF];
        vec1 = CELL_3D[Hash3D(seed, x1, y0, z1) & 0xFF];
        lx0x = Lerp(vec0.x, vec1.x, xs);
        ly0x = Lerp(vec0.y, vec1.y, xs);
        lz0x = Lerp(vec0.z, vec1.z, xs);
        vec0 = CELL_3D[Hash3D(seed, x0, y1, z1) & 0xFF];
        vec1 = CELL_3D[Hash3D(seed, x1, y1, z1) & 0xFF];
        lx1x = Lerp(vec0.x, vec1.x, xs);
        ly1x = Lerp(vec0.y, vec1.y, xs);
        lz1x = Lerp(vec0.z, vec1.z, xs);
        v3.x = v3.x + Lerp(lx0y, Lerp(lx0x, lx1x, ys), zs) * perturbAmp;
        v3.y = v3.y + Lerp(ly0y, Lerp(ly0x, ly1x, ys), zs) * perturbAmp;
        v3.z = v3.z + Lerp(lz0y, Lerp(lz0x, lz1x, ys), zs) * perturbAmp;
    }

    public void GradientPerturb(Vector2f v2) {
        this.SingleGradientPerturb(this.m_seed, this.m_gradientPerturbAmp, this.m_frequency, v2);
    }

    public void GradientPerturbFractal(Vector2f v2) {
        int seed = this.m_seed;
        float amp = this.m_gradientPerturbAmp * this.m_fractalBounding;
        float freq = this.m_frequency;
        this.SingleGradientPerturb(seed, amp, this.m_frequency, v2);
        for (int i = 1; i < this.m_octaves; i++) {
            freq *= this.m_lacunarity;
            amp *= this.m_gain;
            this.SingleGradientPerturb(++seed, amp, freq, v2);
        }
    }

    private void SingleGradientPerturb(int seed, float perturbAmp, float frequency, Vector2f v2) {
        float xf = v2.x * frequency;
        float yf = v2.y * frequency;
        int x0 = FastFloor(xf);
        int y0 = FastFloor(yf);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        float xs;
        float ys;
        switch(this.m_interp) {
            case Linear:
            default:
                xs = xf - (float) x0;
                ys = yf - (float) y0;
                break;
            case Hermite:
                xs = InterpHermiteFunc(xf - (float) x0);
                ys = InterpHermiteFunc(yf - (float) y0);
                break;
            case Quintic:
                xs = InterpQuinticFunc(xf - (float) x0);
                ys = InterpQuinticFunc(yf - (float) y0);
        }
        FastNoise.Float2 vec0 = CELL_2D[Hash2D(seed, x0, y0) & 0xFF];
        FastNoise.Float2 vec1 = CELL_2D[Hash2D(seed, x1, y0) & 0xFF];
        float lx0x = Lerp(vec0.x, vec1.x, xs);
        float ly0x = Lerp(vec0.y, vec1.y, xs);
        vec0 = CELL_2D[Hash2D(seed, x0, y1) & 0xFF];
        vec1 = CELL_2D[Hash2D(seed, x1, y1) & 0xFF];
        float lx1x = Lerp(vec0.x, vec1.x, xs);
        float ly1x = Lerp(vec0.y, vec1.y, xs);
        v2.x = v2.x + Lerp(lx0x, lx1x, ys) * perturbAmp;
        v2.y = v2.y + Lerp(ly0x, ly1x, ys) * perturbAmp;
    }

    public static enum CellularDistanceFunction {

        Euclidean, Manhattan, Natural
    }

    public static enum CellularReturnType {

        CellValue,
        NoiseLookup,
        Distance,
        Distance2,
        Distance2Add,
        Distance2Sub,
        Distance2Mul,
        Distance2Div
    }

    private static class Float2 {

        public final float x;

        public final float y;

        public Float2(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Float3 {

        public final float x;

        public final float y;

        public final float z;

        public Float3(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static enum FractalType {

        FBM, Billow, RigidMulti
    }

    public static enum Interp {

        Linear, Hermite, Quintic
    }

    public static enum NoiseType {

        Value,
        ValueFractal,
        Perlin,
        PerlinFractal,
        Simplex,
        SimplexFractal,
        Cellular,
        WhiteNoise,
        Cubic,
        CubicFractal
    }
}