import {SeasonLight} from "./season-light";
import {TvShowLight} from "./tvshow-light";
import {EpisodeLight} from "./episode-light";

export interface SeasonDetail extends SeasonLight {
  overview: string;
  tvShow: TvShowLight;
  episodes: EpisodeLight[];
}
