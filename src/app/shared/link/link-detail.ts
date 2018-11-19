import {LinkLight} from "./link-light";
export interface LinkDetail extends LinkLight {
   id: number;
   fileName: string;
   parentUrl: string;
   size: number;
   codec: string;
   language: string;
   quality: string;
}
