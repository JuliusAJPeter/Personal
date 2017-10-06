import sys
import spotipy
import spotipy.util as util
import json
import requests


'''
Changelog:
V0.1 - Created with oauth token generated from spotify
V0.2 - Added oauth base url and body_params to access token
V0.3 - fix in distinct track names

****************************
Run as:
python spotifyPlaylist.py <username>
<username> = juliusajp
'''

if len(sys.argv) > 1:
    username = sys.argv[1]
else:
    print "Usage: %s username" % (sys.argv[0],)
    sys.exit()


client_id = '6bb2322c8b264ff991456be50883b70d'
client_secret = '78d38be12c5a4c798137b37610afb434'
grant_type = 'client_credentials'
url = 'https://accounts.spotify.com/api/token'

'''
redirect_uri = 'http://127.0.0.1:8000/callback'
scope = 'user-library-read'
os.environ["SPOTIPY_CLIENT_ID"] = client_id
os.environ["SPOTIPY_CLIENT_SECRET"] = client_secret
os.environ["SPOTIPY_REDIRECT_URI"] = redirect_uri
token = util.prompt_for_user_token(username, scope, client_id, client_secret, redirect_uri)
token = 'BQAkQhjxe2FiRKikKeTIS63JhvgBkFSTOiG-HXlVQWb-iIftObKiSC6yu0ayrBUba2It4mDqDg2-Vsmx8R9AWnk8yiHFSHahoYDqzOX4k19WlmRxo8kHh2GBqJAbDnI_boICaJtJpnhYaVzOUptB_T1XEqzPtzGBoBdu37zxMQ1LciHMRHXLdA'
'''

# oauth token access

body_params = {'grant_type' : grant_type}
response = requests.post(url, data=body_params, auth = (client_id, client_secret))
token_info = response.json()
token = token_info['access_token']

sp = spotipy.Spotify(auth=token)
results = sp.user_playlists(username)
other = []
general = []

for playlist in results['items']:
    playlistId = playlist['id']
    details = sp.user_playlist(username, playlistId)
    for tracks in details['tracks']['items']:
        if playlist['name'] == u'Other':
            other.append(tracks['track']['name'])
        else:
            general.append(tracks['track']['name'])

print('**** Tracks in General ****')
print(general)
print('\n**** Tracks in Other ****')
print(other)
print('\n**** Track names that are in both playlists ****')
print(list(set(other).intersection(set(general))))
print('\n**** Distinct list of all track names ****')

'''V0.1
print(list(set(other).union(set(general)) - set(other).intersection(set(general)) - set(general).intersection(set(other))))
'''

''' V0.2
print(other + general)
'''

print(list(set(other + general)))
