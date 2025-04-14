import sys
import json
from youtube_transcript_api import YouTubeTranscriptApi

def get_video_id(url):
    """Extract video ID from YouTube URL"""
    if "youtube.com/watch?v=" in url:
        return url.split("youtube.com/watch?v=")[1].split("&")[0]
    elif "youtu.be/" in url:
        return url.split("youtu.be/")[1].split("?")[0]
    return url  # Assume it's already a video ID

def get_transcript(video_url):
    try:
        video_id = get_video_id(video_url)
        """ If english is not present then go with hindi """
        transcript_list = YouTubeTranscriptApi.get_transcript(video_id, languages=['en', 'hi'])

        # Format transcript as plain text
        transcript_text = ""
        for entry in transcript_list:
            transcript_text += f"{entry['text']} "

        return transcript_text.strip()
    except Exception as e:
        return f"Error fetching transcript: {str(e)}"

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Please provide a YouTube URL as an argument")
        sys.exit(1)

    youtube_url = sys.argv[1]
    transcript = get_transcript(youtube_url)
    print(transcript)