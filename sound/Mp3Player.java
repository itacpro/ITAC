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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class Mp3Player {
        private static boolean isEnabled = true;
        public static void setEnabled(boolean enabled){
                isEnabled = enabled;
        }
       
        public static void play(final String audioFilePath){
			if(isEnabled){
				try {
					final AdvancedPlayer p = new AdvancedPlayer(new FileInputStream(new File(audioFilePath)));
					Thread t = new Thread(){
						@Override
						public void run() {
							try {
								p.play();
							} catch (JavaLayerException e) {
								e.printStackTrace();
							}
						}
					};
				
					t.setPriority(Thread.MIN_PRIORITY);
					t.start();
				
				} catch (FileNotFoundException e) {
				        e.printStackTrace();
				} catch (JavaLayerException e) {
				        e.printStackTrace();
				}
			}
        }
}