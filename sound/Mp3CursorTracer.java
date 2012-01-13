/***********************************************************************
 *   MT4j Extension: MT4j Sound
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License (LGPL)
 *   as published by the Free Software Foundation, either version 3
 *   of the License, or (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the LGPL
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************/

package sound;

import org.mt4j.input.inputData.MTFingerInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.input.inputProcessors.globalProcessors.AbstractGlobalInputProcessor;

public class Mp3CursorTracer extends AbstractGlobalInputProcessor {
        private String audioFilePath;
        public static int INPUT_DETECTED = MTFingerInputEvt.INPUT_DETECTED;
        public static int INPUT_ENDED = MTFingerInputEvt.INPUT_ENDED;
        private int mode = INPUT_DETECTED;
        public Mp3CursorTracer(String audioFilePath, int mode){
                this.mode = mode;
                this.audioFilePath = audioFilePath;
        }
        public Mp3CursorTracer(String audioFilePath){
                this(audioFilePath, INPUT_DETECTED);
        }
        @Override
        public void processInputEvtImpl(MTInputEvent inputEvent) {
                if(inputEvent instanceof MTFingerInputEvt){
                        MTFingerInputEvt fie = (MTFingerInputEvt)inputEvent;
                        switch (fie.getId()) {
                        case MTFingerInputEvt.INPUT_DETECTED:
                                if(this.mode==INPUT_DETECTED){
                                        Mp3Player.play(this.audioFilePath);
                                }
                                
                                break;
                        case MTFingerInputEvt.INPUT_ENDED:
                                if(this.mode==INPUT_ENDED){
                                        Mp3Player.play(this.audioFilePath);
                                }
                        default:
                                break;
                        }
                }
        }
}
