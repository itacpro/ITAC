/***********************************************************************
 *   MT4j Extension: MTCircularMenu
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

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;

public class Mp3GestureAction implements IGestureEventListener {
        public static final int GESTURE_DETECTED = MTGestureEvent.GESTURE_STARTED;
        public static final int GESTURE_ENDED = MTGestureEvent.GESTURE_ENDED;
        private int triggerType;
        private String audioFilePath;
        
        public Mp3GestureAction(int triggerType, String audioFile){
                this.triggerType = triggerType;
        }
        public Mp3GestureAction(String audioFilePath){
                this.triggerType = GESTURE_DETECTED;
                this.audioFilePath = audioFilePath;
        }
        @Override
        public boolean processGestureEvent(MTGestureEvent ge) {
                switch (ge.getId()) {
                case MTGestureEvent.GESTURE_STARTED:
                        if(this.triggerType==MTGestureEvent.GESTURE_STARTED){
                                Mp3Player.play(this.audioFilePath);
                        }
                        break;
//              case ge.GESTURE_UPDATED:
//                      break;
                case MTGestureEvent.GESTURE_ENDED:
                        if(this.triggerType==MTGestureEvent.GESTURE_ENDED){
                                Mp3Player.play(this.audioFilePath);
                        }
                        break;
                default:
                        break;
                }
                return false;
        }

}
