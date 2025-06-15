<script>
import { ref, onMounted } from "vue";

const backendUrl = import.meta.env.VITE_APP_BACKEND_URL;
const getAllPostsUrl = `${backendUrl}/api/posts`;

export default {
  data() {
    return {
      postTitle: '',
      postContent: '',
      posts: [],
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
        }
      } catch (error) {
        console.error("Failed to fetch posts:", error);
      }
    },

    async submitPost() {
      // Reset previous errors
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
          const result = await response.json();
          console.log('Server response:', result);

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
    }
  },
  mounted() {
    this.fetchAllPosts(); // ✅ fetch on page load
  }
};

</script>

<template>
  <div>
    <h2>Forum</h2>
    <br />
    <p>
      Welkom op de forum pagina van Kieskeurig. <br />
      Heb je een vraag stel die vooral, of beantwoord vragen :D
    </p>
  </div>

  <div>
    <h3>Gesprekken</h3>
    <ul>
      <li class="posts" v-for="post in posts" :key="post.id">
        <strong>{{ post.title }}</strong><br />
        {{ post.content }}
      </li>
    </ul>
  </div>

  <div>
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
</template>

<style>
div {
  margin-top: 20px;
}

.posts {
  margin-top: 20px;
}

.input-error {
  border: 2px solid red;
  outline: none;
}
</style>
