<script>
const backendUrl = import.meta.env.VITE_APP_BACKEND_URL;
const getAllPostsUrl = `${backendUrl}/api/posts`;

export default {
  data() {
    return {
      postTitle: '',
      postContent: '',
      posts: [],
      commentsByPost: {},
      newComment: {},
      errors: {
        title: false,
        content: false
      }
    };
  },

  methods: {
    async fetchAllPosts() {
      try {
        const response = await fetch(getAllPostsUrl);
        if (response.ok) {
          this.posts = await response.json();

          // Fetch comments for each post
          for (const post of this.posts) {
            await this.fetchComments(post.id);
          }
        }
      } catch (error) {
        console.error("Failed to fetch posts:", error);
      }
    },

    async fetchComments(postId) {
      try {
        const response = await fetch(`${backendUrl}/api/comments/${postId}`);
        if (response.ok) {
          const comments = await response.json();
          this.$set(this.commentsByPost, postId, comments);
        }
      } catch (err) {
        console.error("Failed to fetch comments:", err);
      }
    },

    async submitPost() {
      this.errors.title = false;
      this.errors.content = false;

      try {
        const response = await fetch(`${backendUrl}/api/posts/create`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            title: this.postTitle,
            content: this.postContent,
            author: 1
          })
        });

        if (response.ok) {
          this.postTitle = '';
          this.postContent = '';
          await this.fetchAllPosts();
        } else {
          const errorResponse = await response.json();

          if (errorResponse.message?.includes("title")) {
            this.errors.title = true;
          }
          if (errorResponse.message?.includes("content")) {
            this.errors.content = true;
          }

          console.error('Validation error:', errorResponse);
        }
      } catch (error) {
        console.error('Failed to send post:', error);
      }
    },

    async submitComment(postId) {
      const content = this.newComment[postId];
      if (!content) return;

      try {
        const response = await fetch(`${backendUrl}/api/comments`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            postId,
            content,
            author: 1
          })
        });

        if (response.ok) {
          this.newComment[postId] = "";
          await this.fetchComments(postId);
        }
      } catch (err) {
        console.error("Failed to post comment:", err);
      }
    }
  },

  mounted() {
    this.fetchAllPosts();
  }
};
</script>

<template>
  <div>
    <h2>Forum</h2>
    <p>
      Welkom op de forum pagina van Kieskeurig. <br />
      Heb je een vraag stel die vooral, of beantwoord vragen :D
    </p>

    <div class="new-post">
      <h3>Nieuwe Post</h3>
      <input
        v-model="postTitle"
        :class="{ 'input-error': errors.title }"
        placeholder="Type je vraag hier..."
      />
      <input
        v-model="postContent"
        :class="{ 'input-error': errors.content }"
        placeholder="Voor extra context"
      />
      <button @click="submitPost">Post</button>
    </div>

    <div>
      <h3>Gesprekken</h3>
      <ul>
        <li class="posts" v-for="post in posts" :key="post.id">
          <strong>{{ post.title }}</strong><br />
          {{ post.content }}

          <div class="comments" v-if="post.comments && post.comments.length > 0">
            <h4>Reacties:</h4>
            <ul>
              <li v-for="comment in post.comments" :key="comment.id">
                {{ comment.content }}
              </li>
            </ul>
          </div>

          <input
            v-model="newComment[post.id]"
            placeholder="Typ een reactie..."
          />
          <button @click="submitComment(post.id)">Reageer</button>
        </li>

      </ul>
    </div>


  </div>
</template>

<style>
div {
  margin-top: 20px;
}

.posts {
  margin-top: 20px;
  padding: 10px;
  border: 1px solid #ccc;
}

.input-error {
  border: 2px solid red;
  outline: none;
}

.comments {
  margin-top: 10px;
  margin-left: 20px;
  font-size: 0.9em;
}

.new-post {
  margin-top: 30px;
}
</style>
