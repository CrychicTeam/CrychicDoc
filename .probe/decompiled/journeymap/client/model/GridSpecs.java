package journeymap.client.model;

public class GridSpecs {

    public static final GridSpec DEFAULT_DAY = new GridSpec(GridSpec.Style.Squares, 0.5F, 0.5F, 0.5F, 0.5F);

    public static final GridSpec DEFAULT_NIGHT = new GridSpec(GridSpec.Style.Squares, 0.5F, 0.5F, 1.0F, 0.3F);

    public static final GridSpec DEFAULT_UNDERGROUND = new GridSpec(GridSpec.Style.Squares, 0.5F, 0.5F, 0.5F, 0.3F);

    private GridSpec day;

    private GridSpec night;

    private GridSpec underground;

    public GridSpecs() {
        this(DEFAULT_DAY.clone(), DEFAULT_NIGHT.clone(), DEFAULT_UNDERGROUND.clone());
    }

    public GridSpecs(GridSpec day, GridSpec night, GridSpec underground) {
        this.day = day;
        this.night = night;
        this.underground = underground;
    }

    public GridSpec getSpec(MapType mapType) {
        switch(mapType.name) {
            case day:
                return this.day;
            case night:
                return this.night;
            case underground:
                return this.underground;
            default:
                return this.day;
        }
    }

    public void setSpec(MapType mapType, GridSpec newSpec) {
        switch(mapType.name) {
            case day:
                this.day = newSpec.clone();
                return;
            case night:
                this.night = newSpec.clone();
                return;
            case underground:
                this.underground = newSpec.clone();
                return;
            default:
                this.day = newSpec.clone();
        }
    }

    public GridSpecs clone() {
        return new GridSpecs(this.day.clone(), this.night.clone(), this.underground.clone());
    }

    public void updateFrom(GridSpecs other) {
        this.day = other.day.clone();
        this.night = other.night.clone();
        this.underground = other.underground.clone();
    }
}