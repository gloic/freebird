import {Pipe, PipeTransform} from '@angular/core';

@Pipe({name: 'fileCodec'})
export class FileCodecPipe implements PipeTransform {

  transform(entry: string): string {
    return entry.replace("CODEC_", "");
  }
}
