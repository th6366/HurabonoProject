#ifndef KOREA_TREE
#define KOREA_TREE
template<typename T>
class korea_tree{
	private:
		struct node{
			node *ch[2];
			T data;
			int s;
			node(const T&d):data(d),s(1){}
			node():s(0){ch[0]=ch[1]=this;}
		}*nil,*root,t;
		int max_deep,d;
		void insert(node *&o,const T&data){
			if(!o->s){
				o=new node(data);
				o->ch[0]=o->ch[1]=nil;
			}else o->s++,d++,insert(o->ch[o->data<data],data);
		}
		inline void success_erase(node *&o){
			node *t=o;
			o=o->ch[0]->s?o->ch[0]:o->ch[1];
			delete t;
		}
		bool erase(node *&o,const T&data){
			if(!o->s)return 0;
			if(o->data==data){
				if(!o->ch[0]->s||!o->ch[1]->s)success_erase(o);
				else{
					o->s--;
					node **t=&o->ch[1];
					for(;(*t)->ch[0]->s;t=&(*t)->ch[0])(*t)->s--;
					o->data=(*t)->data;
					success_erase(*t);
				}return 1;
			}else if(erase(o->ch[o->data<data],data)){
				o->s--;return 1;
			}else return 0;
		}
		node *flatten(node *x,node *y){
			if(!x->s)return y;
			x->ch[1]=flatten(x->ch[1],y);
			return flatten(x->ch[0],x);
		}
		node *build(node *x,int n){
			if(!n){
				x->ch[0]=nil;
				return x;
			}
			node *y=build(x,n/2);
			node *z=build(y->ch[1],n-1-n/2);
			y->ch[1]=z->ch[0];
			z->ch[0]=y;
			y->s=y->ch[0]->s+y->ch[1]->s+1;
			return z;
		}
		void clear(node *&p){
			if(p->s)clear(p->ch[0]),clear(p->ch[1]),delete p;
		}
	public:
		korea_tree(int d=1000):nil(new node),root(nil),max_deep(d){}
		~korea_tree(){clear(root),delete nil;}
		inline void clear(){clear(root),root=nil;}
		inline void insert(const T&data){
			d=0;
			insert(root,data);
			if(d>=max_deep)rebuild();
		}
		inline bool erase(const T&data){
			return erase(root,data);
		}
		inline void rebuild(){
			root=build(flatten(root,&t),root->s)->ch[0];
		}
		inline bool find(const T&data){
			node *o=root;
			while(o->s&&o->data!=data)o=o->ch[o->data<data];
			return o->s;
		}
		inline int rank(const T&data){
			int cnt=0;
			for(node *o=root;o->s;)
			if(o->data<data)cnt+=o->ch[0]->s+1,o=o->ch[1];
			else o=o->ch[0];
			return cnt;
		}
		inline const T&kth(int k){
			for(node *o=root;;)
			if(k<=o->ch[0]->s)o=o->ch[0];
			else if(k==o->ch[0]->s+1)return o->data;
			else k-=o->ch[0]->s+1,o=o->ch[1];
		}
		inline const T&operator[](int k){
			return kth(k);
		}
		inline const T&preorder(const T&data){
			node *x=root,*y=0;
			while(x->s)
			if(x->data<data)y=x,x=x->ch[1];
			else x=x->ch[0];
			if(y)return y->data;
			return data;
		}
		inline const T&successor(const T&data){
			node *x=root,*y=0;
			while(x->s)
			if(data<x->data)y=x,x=x->ch[0];
			else x=x->ch[1];
			if(y)return y->data;
			return data;
		}
		inline int size(){return root->s;}
};
#endif
