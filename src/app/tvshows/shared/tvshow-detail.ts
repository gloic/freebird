import {SeasonDetail} from "./season-detail";
import {TvShowLight} from "./tvshow-light";

export interface TvShowDetail extends TvShowLight {
  overview: string;
  seasons: SeasonDetail[];
  firstAirDate: string;
  popularity: number;
}
